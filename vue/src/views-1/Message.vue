<template>
  <div class="message-main">
    <Header></Header>
    <div class="panel">
      <div class="user-list">
        <div class="search">
          <input placeholder="搜索联系人" />
        </div>
        <el-scrollbar height="82vh">
          <UserList
            v-for="(item, index) in user"
            class="user-list-item"
            :id="item.id"
            :nameFrom="item.nameFrom"
            :nameTo="item.nameTo"
            :content="item.content"
            :idxFrom="item.idxFrom"
            :idxTo="item.idxTo"
            :photoFromUrl="item.photoFromUrl"
            :photoToUrl="item.photoToUrl"
            :isCheck="item.isCheck"
            :type="item.type"
            :gmtModify="relativeDate(item.gmtModify)"
            @click="
              chatClick(
                item.idxFrom,
                item.idxTo,
                item.nameFrom,
                item.nameTo,
                index,
                item.id
              )
            "
          ></UserList>
        </el-scrollbar>
      </div>
      <div class="message-panel">
        <div class="name">{{ nameCurrent }}</div>
        <div class="message-list">
          <el-scrollbar height="60vh">
            <MessageList
              v-for="item in message"
              class="message-list-item"
              :nameFrom="item.nameFrom"
              :nameTo="item.nameTo"
              :content="item.content"
              :idxFrom="item.idxFrom"
              :idxTo="item.idxTo"
              :photoFromUrl="item.photoFromUrl"
              :gmtCreate="relativeDate(item.gmtCreate)"
              :type="item.type"
            ></MessageList>
            <div>
              <a id="end" name="1" href="#1"></a>
            </div>
          </el-scrollbar>
        </div>
        <div class="text">
          <div class="type">
            <div class="item" @click="changeMsgType(1)">文本</div>
            <div class="item" @click="changeMsgType(2)">图片</div>
          </div>
          <div class="txt" v-if="msgtype === 1">
            <div class="text-bg">
              <textarea v-model="content"></textarea>
            </div>
            <div class="button" @click="send()">发送</div>
          </div>
          <div class="png" v-if="msgtype === 2">
            <div
              class="file-button"
              @click="selectFile()"
              @drop="dropFile($event)"
              @dragover="dragOverFile($event)"
            >
              <div class="icon">
                <el-icon><Plus /></el-icon>
              </div>
              <div class="imp">拖拽图片上传或<span> 点击此处</span></div>
            </div>
            <div class="imp-bottom">支持png/jpg格式，大小不超过5MB</div>
            <input
              type="file"
              name="file"
              accept="image/*"
              @change="getFile"
              ref="inputer"
              class="file-input"
            />
          </div>
        </div>
      </div>
      <div class="other"></div>
    </div>
    <div class="panel-bg"></div>
  </div>
</template>
<script setup>
import Header from "@/components-1/Header.vue";
import UserList from "@/components-1/UserList.vue";
import MessageList from "@/components-1/MessageList.vue";
import { relativeDate } from "@/utils/getDate";
import { ElMessage, ElNotification } from "element-plus";
import { ref, getCurrentInstance, onMounted } from "vue";
import { getCookie } from "@/utils/cookies";
import { getDate } from "@/utils/getDate";
const { proxy } = getCurrentInstance();
let user = ref([]);
let message = ref([]);
let websocket;
let curIndex;
let url;
let msgtype = ref(1);
let idxCurrent = ref(0);
let nameCurrent = ref();
let content = ref();
let newimgurl = ref();
let file;
let userName = ref(
  getCookie("userName").userName ? getCookie("userName").userName : "未登录"
);
let idxUser = ref(
  Number(getCookie("idxUser").idxUser)
    ? Number(getCookie("idxUser").idxUser)
    : 0
);
let idxPhoto = ref(
  Number(getCookie("idxPhoto").idxPhoto)
    ? Number(getCookie("idxPhoto").idxPhoto)
    : 0
);

let connecturl = `wss://dialogue.org.cn/websocket/ws/${idxUser.value}`;
//let connecturl = `ws://localhost:8080/ws/${idxUser.value}`;

const changeMsgType = (num) => {
  msgtype.value = num;
};

const selectFile = () => {
  let fileInput = document.getElementsByClassName("file-input")[0];
  fileInput.click();
};

const dropFile = (e) => {
  e.preventDefault();
  file = e.dataTransfer.files[0];
  if (file !== undefined) {
    if (file.size > 1024 * 1024) {
      ElMessage({
        showClose: false,
        message: "图片过大",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: true,
      });
    } else {
      let formData = new FormData();
      formData.append("file", file);
      proxy.$axios
        .post("/image/insert/oss", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          if (response.status === 200 && response.data) {
            newimgurl.value = response.data.data;
            sendPng();
          }
        })
        .catch((error) => {

        });
    }
  }
};

const dragOverFile = (e) => {
  e.preventDefault();
};

const getFile = () => {
  let inputDOM = proxy.$refs.inputer;
  file = inputDOM.files[0];
  if (file !== undefined) {
    if (file.size > 1024 * 1024) {
      ElMessage({
        showClose: false,
        message: "图片过大",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: true,
      });
    } else {
      let formData = new FormData();
      formData.append("file", file);
      proxy.$axios
        .post("/image/insert/oss", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          if (response.status === 200 && response.data) {
            newimgurl.value = response.data.data;
            sendPng();
          }
        })
        .catch((error) => {

        });
    }
  }
};

const send = () => {
  if (idxCurrent.value === 0) {
    content.value = "";
    ElNotification({
      title: "发送失败",
      message: "请选择发送对象",
      type: "warn",
      duration: 2000,
    });
  } else {
    if (content.value === "") {
      ElNotification({
        title: "发送失败",
        message: "内容不能为空",
        type: "warn",
        duration: 2000,
      });
    } else {
      proxy.$axios
        .post("/message/send", {
          idxFrom: idxUser.value,
          nameFrom: userName.value,
          idxTo: idxCurrent.value,
          nameTo: nameCurrent.value,
          photoFrom: idxPhoto.value,
          content: content.value,
          gmtCreate: getDate(),
          type: 1,
        })
        .then((response) => {
          if (response.status === 200 && response.data.code === 1) {
            chat(
              idxUser.value,
              idxCurrent.value,
              userName.value,
              nameCurrent.value
            );
            user.value[curIndex].content = content.value;
            content.value = "";
          }
        })
        .catch((error) => {});
    }
  }
};

const sendPng = () => {
  if (idxCurrent.value === 0) {
    ElNotification({
      title: "发送失败",
      message: "请选择发送对象",
      type: "warn",
      duration: 2000,
    });
  } else {
    if (newimgurl.value === "") {
      ElNotification({
        title: "发送失败",
        message: "内容不能为空",
        type: "warn",
        duration: 2000,
      });
    } else {
      proxy.$axios
        .post("/message/send", {
          idxFrom: idxUser.value,
          nameFrom: userName.value,
          idxTo: idxCurrent.value,
          nameTo: nameCurrent.value,
          photoFrom: idxPhoto.value,
          content: newimgurl.value,
          gmtCreate: getDate(),
          type: 2,
        })
        .then((response) => {
          if (response.status === 200 && response.data.code === 1) {
            chat(
              idxUser.value,
              idxCurrent.value,
              userName.value,
              nameCurrent.value
            );
            user.value[curIndex].content = "[图片]";
          }
        })
        .catch((error) => {
          ElNotification({
            title: "发送失败",
            message: error,
            type: "error",
            duration: 2000,
          });
        });
      newimgurl.value = "";
    }
  }
};

const checkMessage = (id) => {
  url = `/message/check?id=${id}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
      }
    })
    .catch((error) => {
      ElNotification({
        title: "读取失败",
        message: error,
        type: "error",
      });
    });
};

const chat = (from, to, nameFrom, nameTo) => {
  from = Number(from);
  to = Number(to);
  if (getCookie("token").token) {
    if (idxUser.value === from) {
      idxCurrent.value = to;
      nameCurrent.value = nameTo;
    } else {
      idxCurrent.value = from;
      nameCurrent.value = nameFrom;
    }
    proxy.$axios
      .post("/message/get", {
        idxFrom: Number(from),
        idxTo: Number(to),
        pageCurrent: 1,
        pageSize: 10,
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          message.value = response.data.data;
          let end = document.getElementById("end");
          end.click();
        }
      })
      .catch((error) => {
        ElNotification({
          title: "获取失败",
          message: error,
          type: "error",
          duration: 2000,
        });
      });
  } else {
    ElMessage({
      showClose: false,
      message: "登录失效",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: true,
    });
    proxy.$router.push({ name: "login" });
  }
};

const chatClick = (from, to, nameFrom, nameTo, index, id) => {
  if (user.value[index].isCheck === 0) {
    user.value[index].isCheck = 1;
    checkMessage(id);
    curIndex = index;
  }
  from = Number(from);
  to = Number(to);
  if (getCookie("token").token) {
    if (idxUser.value === from) {
      idxCurrent.value = to;
      nameCurrent.value = nameTo;
    } else {
      idxCurrent.value = from;
      nameCurrent.value = nameFrom;
    }
    proxy.$axios
      .post("/message/get", {
        idxFrom: Number(from),
        idxTo: Number(to),
        pageCurrent: 1,
        pageSize: 10,
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          message.value = response.data.data;
          let end = document.getElementById("end");
          end.click();
        }
      })
      .catch((error) => {
        ElNotification({
          title: "获取失败",
          message: error,
          type: "error",
          duration: 2000,
        });
      });
  } else {
    ElMessage({
      showClose: false,
      message: "登录失效",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: true,
    });
    proxy.$router.push({ name: "login" });
  }
};

onMounted(() => {
  if (getCookie("token").token) {
    proxy.$axios
      .post("/message/get/contact", {
        idxUser: idxUser.value,
        pageCurrent: 1,
        pageSize: 8,
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          user.value = response.data.data;
        }
      })
      .catch((error) => {

      });
  } else {
    ElMessage({
      showClose: false,
      message: "登录失效",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: true,
    });
    proxy.$router.push({ name: "login" });
  }
});

if ("WebSocket" in window) {
  websocket = new WebSocket(connecturl);
} else {

}

websocket.onerror = function () {

};

websocket.onopen = function () {};

websocket.onmessage = function (event) {
  if (event.data) {
    let id = Number(event.data);
    if (idxCurrent.value === id) {
      setTimeout(() => {
        chat(
          idxCurrent.value,
          idxUser.value,
          nameCurrent.value,
          userName.value
        );
      }, 1000);
    }
    proxy.$axios
      .post("/message/get/contact", {
        idxUser: idxUser.value,
        pageCurrent: 1,
        pageSize: 8,
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          user.value = response.data.data;
        }
      })
      .catch((error) => {

      });
  }
};

window.onbeforeunload = function (e) {
  e = e || window.event;
  if (e) {
    e.returnValue = "关闭提示";
  }
  websocket.close();
};
</script>

<style scoped>
.message-main {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow-x: hidden;
}
.message-main .panel {
  width: 100%;
  height: 92vh;
  display: flex;
  flex-direction: row;
  overflow: hidden;
}
.message-main .panel-bg {
  position: fixed;
  left: 0%;
  width: 100%;
  top: 8vh;
  height: 92vh;
  opacity: 1;
  background-color: #fde2e2;
  z-index: -2;
}
.message-main .panel .user-list {
  width: 20%;
  height: 92vh;
  overflow: hidden;
}
.user-list .search {
  margin-top: 3vh;
  margin-left: 20px;
  margin-bottom: 1vh;
}
.user-list input {
  width: 90%;
  height: 4vh;
  font-size: 18px;
  border: none;
  outline: none;
  background-color: transparent;
  border-bottom: 1px solid red;
  text-indent: 5px;
}
.message-main .panel .message-panel {
  width: 50%;
  height: 92vh;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
}
.message-panel .name {
  user-select: none;
  padding-top: 2vh;
  padding-bottom: 1vh;
  height: 3vh;
  width: 100%;
  text-align: center;
  font-weight: 500;
  font-size: 18px;
  border-bottom: 1px solid #fde2e2;
  background-color: rgba(255, 255, 255, 0.5);;
}
.message-panel .message-list {
  width: 100%;
  height: 70%;
  background-color: rgba(255, 255, 255, 0.5);
  overflow: hidden;
  border-bottom-left-radius: 8px;
  border-bottom-right-radius: 8px;
}
.message-panel .text {
  width: 100%;
  height: 30%;
  display: flex;
  flex-direction: column;

}
.message-panel .text .type {
  display: flex;
  user-select: none;
  height: 10%;
  padding-top: 1vh;
  padding-bottom: 1vh;
  padding-left: 10px;
  font-size: 14px;
  font-weight: 400;
}
.message-panel .text .type .item {
  margin-right: 10px;
  border-radius: 5px;
  width: 30px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.message-panel .text .txt {
  height: 85%;
  margin-bottom: 10px;
}
.message-panel .text .txt .text-bg {
  height: 85%;
  padding-top: 1vh;
  padding-left: 10px;
  padding-right: 10px;
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 8px;
}
.message-panel .text .txt textarea {
  outline: none;
  resize: none;
  border: none;
  padding-top: 5px;
  padding-right: 10px;
  width: 100%;
  height: calc(100% - 5px);
  font-size: 16px;
  background-color: transparent;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
.message-panel .text .txt .button {
  text-align: center;
  margin-left: 90%;
  cursor: pointer;
  user-select: none;
  width: 10%;
  color: white;
  background: conic-gradient(
    from 120deg at 50% 50%,
    #790e09,
    #961a09,
    #b32b09,
    #d03f09,
    #ef5807,
    #fa7819,
    #fd9833,
    #ffb44e
  );
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  border-radius: 5px;
}
.message-panel .text .txt .button:hover {
  transition: all 0.3s ease;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.35);
}
.message-list-item:nth-child(1) {
  margin-top: 2vh;
}

.file-input {
  outline: none;
  border: none;
  text-align: center;
  display: none;
}

.file-button {
  width: calc(100% - 3px);
  height: 85%;
  border-radius: 8px;
  border: 2px dashed rgba(0, 0, 0, 0.3);
  transition: all 0.2s ease;
}
.file-button:hover {
  border: 2px dashed rgb(214, 31, 31);
}
.imp {
  text-align: center;
  margin-top: 2vh;
  width: 100%;
  font-size: 14px;
  margin-bottom: 2vh;
}
.imp span {
  color: rgb(214, 31, 31);
}
.file-button .icon {
  width: 100%;
  height: 5vh;
  margin-top: 3vh;
  font-size: 30px;
  color: rgb(214, 31, 31);
  font-weight: 100;
  text-align: center;
}
.imp-bottom {
  text-align: center;
  font-size: 12px;
  margin-top: 1vh;
  color: rgba(0, 0, 0, 0.5);
}
.message-panel .text .png .button {
  text-align: center;
  margin-left: 90%;
  cursor: pointer;
  user-select: none;
  width: 10%;
  color: white;
  background: conic-gradient(
    from 120deg at 50% 50%,
    #790e09,
    #961a09,
    #b32b09,
    #d03f09,
    #ef5807,
    #fa7819,
    #fd9833,
    #ffb44e
  );
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  border-radius: 5px;
}
.message-panel .text .png .button:hover {
  transition: all 0.3s ease;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.35);
}
</style>
