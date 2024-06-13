<template>
  <div class="header-main">
    <div class="title" @click="openHome()">对话</div>
    <div class="search">
      <input
        v-model="key"
        placeholder="请输入搜索内容"
        @keyup.enter="openSearch()"
      />
      <div class="icon" @click="openSearch()">
        <el-icon><Search /></el-icon>
      </div>
    </div>
    <div class="tool">
      <div class="user-info" @mouseleave="changeHover(false)">
        <div
          class="photo"
          @mouseenter="changeHover(true)"
          @click="openUserInfo()"
        >
          <img :src="idxPhotoUrl" class="image" v-if="idxPhoto !== 0" />
          <img src="../assets/pic-8.jpg" class="image" v-if="idxPhoto === 0" />
        </div>
        <div class="info" v-if="isHover">
          <div class="user">
            <div class="name">{{ userName }}</div>
          </div>
          <div class="info-tool">
            <div class="info-item" @click="openUserInfo()">
              <div class="icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="text">个人中心</div>
            </div>
            <div class="info-item" @click="openUserInfo()">
              <div class="icon">
                <el-icon><Star /></el-icon>
              </div>
              <div class="text">我的收藏</div>
            </div>
            <div class="info-item" @click="openUserInfo()">
              <div class="icon">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="text">浏览历史</div>
            </div>
            <div class="info-item" @click="exit()">
              <div class="icon">
                <el-icon><SwitchButton /></el-icon>
              </div>
              <div class="text">退出登录</div>
            </div>
          </div>
        </div>
      </div>

      <div class="item" @click="openUserMessage()">
        <div class="icon">
          <el-badge
            :value="messageUncheck"
            :max="9"
            class="top"
            v-if="messageUncheck !== 0"
          >
            
            <div class="message-icon"></div>
          </el-badge>
          <div v-if="messageUncheck === 0" class="message-icon"></div>
        </div>
        <div class="text">消息</div> 
      </div>
      <div class="item" @click="openEdit()">
        <div class="icon">      
          <div class="edit-icon"></div>
        </div>    
        <div class="text">投稿</div>
      </div>
      
    </div>
  </div>
</template>
<script setup>
import { storeToRefs } from "pinia";
import { ref, getCurrentInstance, onMounted } from "vue";
import { getCookie, removeCookie } from "@/utils/cookies";
import { ElMessage } from "element-plus";
import { useUserStore } from "@/stores/user";
const { proxy } = getCurrentInstance();
const userStore = useUserStore();
let key = ref("禅师");
const { messageUncheck } = storeToRefs(userStore);
let isHover = ref(false);
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
let idxPhotoUrl = ref(
  getCookie("idxPhotoUrl").idxPhotoUrl
    ? getCookie("idxPhotoUrl").idxPhotoUrl
    : 0
);
const changeHover = (state) => {
  isHover.value = state;
};
const openHome = () => {
  proxy.$router.push({ name: "home" });
};
const openSearch = () => {
  if (key.value) {
    let searchRoute = proxy.$router.resolve({
      name: "search",
      query: {
        key: encodeURIComponent(key.value),
        orderType: 1,
        pageCurrent: 1,
        pageSize: 10,
      },
    });
    window.open(searchRoute.href, "_blank");
  } else {
    ElMessage({
      showClose: false,
      message: "搜索内容不能为空",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: false,
    });
  }
};

const openUserInfo = () => {
  if (getCookie("token").token) {
    let userRoute = proxy.$router.resolve({
      name: "user",
      query: {
        id: idxUser.value,
      },
    });
    window.open(userRoute.href, "_blank");
  } else {
    proxy.$router.push({ name: "login" });
  }
};

const openUserMessage = () => {
  if (getCookie("token").token) {
    let messageRoute = proxy.$router.resolve({
      name: "message",
      query: {
        userName: encodeURIComponent(userName.value),
      },
    });
    window.open(messageRoute.href, "_blank");
  } else {
    proxy.$router.push({ name: "login" });
  }
};

const openEdit = () => {
  if (getCookie("token").token) {
    let editRoute = proxy.$router.resolve({
      name: "edit",
      query: {
        userName: encodeURIComponent(userName.value),
      },
    });
    window.open(editRoute.href, "_blank");
  } else {
    proxy.$router.push({ name: "login" });
  }
};

const exit = () => {
  if (idxUser.value === 0) {
    ElMessage({
      showClose: false,
      message: "请先登录",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: true,
    });
  } else {
    removeCookie("userName");
    removeCookie("idxUser");
    removeCookie("token");
    removeCookie("idxPhoto");
    removeCookie("idxPhotoUrl");
    userStore.$patch({
      messageUncheck: 0,
    });
    proxy.$router.push({ name: "login" });
  }
};

</script>
<style scoped>
@font-face {
  font-family: mao;
  src: url("https://dialogue-oss.oss-cn-wulanchabu.aliyuncs.com/maozedong.ttf");
}
@keyframes icon {
  0% {
    margin-top: 0px;
    margin-bottom: 0px;
  }
  50% {
    margin-top: -5px;
    margin-bottom: 5px;
  }
  100% {
    margin-top: 0px;
    margin-bottom: 0px;
  }
}
.header-main {
  width: 100%;
  height: 8vh;
  display: flex;
}
.header-main .title {
  width: 10%;
  font-size: 35px;
  font-family: mao;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color:white;
  filter: drop-shadow(1px 1px 1px rgb(0, 0, 0, 0.4));
  cursor: pointer;
  user-select: none;
}
.header-main .search {
  margin-left: 20%;
  margin-top: 8vh;
  padding: 5px;
  width: 40%;
  height: 60%;
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
  display: flex;
  border-radius: 22px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  justify-content: space-between;
}
.header-main .search input {
  outline: none;
  width: 95%;
  border: 0;
  text-indent: 12px;
  font-size: 18px;
  font-weight: 500;
  border-radius: 18px;
  background-color: rgba(255,255,255,0.8);
  transition: all 0.2s ease;
}
.header-main .search input:hover {
  background-color: white;
  transition: all 0.2s ease;
}
.header-main .search .icon {
  padding: 1vh;
  border-radius: 50%;
  margin-left: 6px;
  width: 3vh;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 22px;
  background-color: rgba(255,255,255,0.8);
  transition: all 0.2s ease;
}
.header-main .search .icon:hover {
  background-color: white;
  transition: all 0.2s ease;
}
.header-main .tool {
  margin-left: 10%;
  display: flex;
}
.header-main .tool .photo {
  width: 5vh;
  max-width: 6vh;
  height: 5vh;
  cursor: pointer;
  margin-top: 1.5vh;
  margin-bottom: 1.5vh;
  margin-right: 1vh;
  border-radius: 50%;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.5s ease;
  user-select: none;
}
.image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  user-select: none;
}
.user-info {
  width: 5vh;
  padding-left: 3vh;
  height: 8vh;
}
.header-main .tool .photo:hover {
  width: 6vh;
  height: 6vh;
  margin-top: 1vh;
  margin-bottom: 1vh;
  margin-right: 0vh;
}
.header-main .tool .item {
  margin-left: 18px;
  user-select: none;
  display: flex;
  cursor: pointer;
  text-align: center;
  flex-direction: column;
  justify-content: center;
}
.header-main .tool .item .icon {
  color: white;
  font-size: 28px;
  font-weight: 500;
  filter: drop-shadow(1px 1px 1px rgb(0, 0, 0, 0.2));
  transition: all 0.5s ease;
}
.header-main .tool .item .icon:hover {
  animation-name: icon;
  animation-duration: 0.3s;
  animation-timing-function: ease;
  animation-iteration-count: 1;
}
.header-main .tool .item .text {
  margin-top: -6px;
  font-size: 12px;
  font-weight: 400;
  color: white;
}
.info {
  width: 21vh;
  margin-left: -9.5vh;
  height: 30vh;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  background-color: white;
  border-radius: 8px;
  padding-top: 3vh;
  padding-left: 2vh;
  padding-right: 2vh;
  z-index: 10;
  transition: all 0.2s ease;
  position: relative;
}
.info .user {
  text-align: center;
  font-size: 18px;
  margin-bottom: 2vh;
  cursor: pointer;
  user-select: none;
  color: rgb(215, 29, 29);
  font-weight: 600;
}
.info .info-tool {
  display: flex;
  flex-direction: column;
}
.info .info-tool .info-item {
  padding-left: 10%;
  padding-right: 10%;
  cursor: pointer;
  user-select: none;
  display: flex;
  width: 80%;
  height: 3vh;
  padding-bottom: 1vh;
  padding-top: 1vh;
  border-radius: 8px;
  margin-bottom: 1vh;
  transition: all 0.3s ease;
}
.info-item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transform: translateX(5px);
  transition: all 0.3s ease;
  background-color: #fde2e2;
}
.info .info-item .icon {
  color: rgb(214, 31, 31);
  font-size: 22px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-right: 20px;
}
.info-item .text {
  cursor: pointer;
}
:deep(.el-badge__content--danger) {
  background-color: rgb(214, 31, 31);
}
:deep(.el-badge__content) {
  border: none;
}
.message-icon{
  min-width: 30px;
  width: 30px;
  height: 30px;
  min-height: 30px;
  margin-bottom: 5px;
  background-image: url('../assets/message.png');
  background-size: contain;
  background-repeat: no-repeat;
}
.edit-icon{
  min-width: 30px;
  min-height: 30px;
  margin-bottom: 5px;
  background-image: url('../assets/edit.png');
  background-size: contain;
  background-repeat: no-repeat;
}
</style>
