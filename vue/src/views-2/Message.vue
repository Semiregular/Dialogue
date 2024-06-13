<template>
  <div class="message-main">
    <Header :class="{ 'header-active': !isuserlist }"></Header>
    <div class="panel">
      <div :class="{ 'user-list': true, 'user-list-active': !isuserlist }">
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
      <div
        :class="{ 'message-panel': true, 'message-panel-active': ischatlist }"
      >
        <div class="top">
          <div class="icon" @click="back()">
            <el-icon><Back /></el-icon>
          </div>
          <div class="name">{{ nameCurrent }}</div>
        </div>

        <div class="message-list">
          <MessageList
            v-for="item in message"
            class="message-list-item"
            :nameFrom="item.nameFrom"
            :nameTo="item.nameTo"
            :content="item.content"
            :idxFrom="item.idxFrom"
            :idxTo="item.idxTo"
            :photoFromUrl="item.photoFromUrl"
            :type="item.type"
            :gmtCreate="relativeDate(item.gmtCreate)"
          ></MessageList>
          <div>
            <a id="end" name="1" href="#1"></a>
          </div>
        </div>
        <div class="text">
          <div class="txt">
            <div class="text-bg">
              <textarea v-model="content"></textarea>
            </div>
            <div class="button" @click="send()">发送</div>
          </div>
        </div>
      </div>
      <div class="other"></div>
    </div>
    <div class="panel-bg"></div>
  </div>
</template>
<script setup>
import Header from "@/components-2/Header.vue";
import UserList from "@/components-2/UserList.vue";
import MessageList from "@/components-2/MessageList.vue";
import { relativeDate } from "@/utils/getDate";
import { ElMessage, ElNotification } from "element-plus";
import { ref, getCurrentInstance, onMounted } from "vue";
import { getCookie } from "@/utils/cookies";
import { getDate } from "@/utils/getDate";
const { proxy } = getCurrentInstance();
let isuserlist = ref(true);
let ischatlist = ref(false);
let user = ref([]);
let message = ref([]);
let websocket;
let url;
let idxCurrent = ref(0);
let nameCurrent = ref();
let content = ref();
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
const back = () => {
  ischatlist.value = false;
  isuserlist.value = true;
};

let connecturl = `wss://dialogue.org.cn/websocket/ws/${idxUser.value}`;
//let connecturl = `ws://localhost:8080/ws/${idxUser.value}`;

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
            setTimeout(() => {
              chat(
                idxUser.value,
                idxCurrent.value,
                userName.value,
                nameCurrent.value
              );
            });
          }
        })
        .catch((error) => {
        });
      content.value = "";
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
          ischatlist.value = true;
          isuserlist.value = false;
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

window.onload = () => {
  let flexheight = document.documentElement.clientHeight;
  let div2 = document.getElementsByClassName("message-main")[0];
  div2.style.height = `${flexheight}px`;
  let div1 = document.getElementsByClassName("message-panel")[0];
  div1.style.height = `${flexheight}px`;
};

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
        ElNotification({
          title: "获取失败",
          message: error,
          type: "error",
          duration: 2000,
        });
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
  opacity: 0.5;
  background-image: url("../assets/pic-1.jpeg");
  background-position: center top;
  background-repeat: no-repeat;
  background-size: 110% 150%;
  z-index: -2;
}
.message-main .panel .user-list {
  width: 100%;
  height: 92vh;
  overflow: hidden;
  transition: all 0.5s ease;
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
  width: 100%;
  height: 100vh;
  position: fixed;
  top: 0%;
  left: 100%;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  opacity: 1;
  transition: all 0.5s ease;
  z-index: 5;
}
.message-panel .top {
  user-select: none;
  padding-top: 2.5vh;
  padding-bottom: 2vh;
  height: 3vh;
  width: 100%;
  display: flex;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  border-bottom: 1px solid #eee;
}
.header-active {
  transform: translateX(-100%);
  transition: all 0.5s ease;
  opacity: 0;
}
.top .name {
  font-weight: 500;
  font-size: 20px;
  justify-content: center;
  display: flex;
  flex-direction: column;
  width: calc(90% - 6vh);
  text-align: center;
}
.message-panel .message-list {
  width: calc(100% - 20px);
  padding-left: 10px;
  padding-right: 10px;
  height: calc(86vh);
  background-color: rgba(255, 255, 255, 0.5);
  overflow: scroll;
}
.message-panel .text {
  width: 100%;
  margin-top: 1vh;
  margin-bottom: 1vh;
  margin-left: 5px;
  margin-right: 5px;
  height: 4vh;
  display: flex;
  flex-direction: column;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.message-panel .top .icon {
  width: 3vh;
  height: 3vh;
  margin-left: 5vw;
  justify-content: center;
  display: flex;
  flex-direction: column;
  text-align: center;
  font-weight: 600;
  font-size: 30px;
}
.message-panel .text .txt {
  height: 100%;
  display: flex;
}
.message-panel .text .txt .text-bg {
  height: 100%;
  width: calc(100% - 80px);
  padding-left: 10px;
  padding-right: 10px;
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 8px;
}
.message-panel .text .txt textarea {
  outline: none;
  resize: none;
  border: none;
  padding-top: 1vh;
  padding-bottom: 5px;
  padding-right: 10px;
  width: 100%;
  height: calc(100% - 5px - 1vh);
  font-size: 16px;
  background-color: transparent;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
.message-panel .text .txt .button {
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 10px;
  margin-right: 10px;
  width: 50px;
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
  margin-top: 3vh;
}
.user-list-active {
  transition: all 0.5s ease;
  transform: translateX(-50%);
  opacity: 0;
}
.message-panel-active {
  opacity: 1;
  transition: all 0.5s ease;
  transform: translateX(-100%);
}
</style>
