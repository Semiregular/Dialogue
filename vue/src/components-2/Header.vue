<template>
  <div class="out">
    <div class="header-main">
      <div class="title" @click="openHome()"></div>
      <div class="button" @click="buttonActive()">
        <div :class="{ line: true, firstline: isactive }"></div>
        <div :class="{ line: true, secondline: isactive }"></div>
        <div :class="{ line: true, thirdline: isactive }"></div>
      </div>
    </div>
    <div :class="{ sidebar: true, 'sidebar-active': isactive }">
      <div class="sidebar-main">
        <div class="user" @click="openUserInfo()">
          <div class="photo">
            <img :src="idxPhotoUrl" class="image" v-if="idxPhoto !== 0" />
            <img
              src="../assets/pic-8.jpg"
              class="image"
              v-if="idxPhoto === 0"
            />
          </div>
          <div class="name">{{ userName }}</div>
        </div>
        <div class="sidebar-wrapper">
          <div class="sidebar-wrapper-item" @click="openUserArticle()">
            <div class="icon">
              <el-icon><Collection /></el-icon>
            </div>
            <div class="text">我的发布</div>
          </div>
          <div class="sidebar-wrapper-item" @click="openUserCollect()">
            <div class="icon">
              <el-icon><Star /></el-icon>
            </div>
            <div class="text">我的收藏</div>
          </div>
          <div class="sidebar-wrapper-item" @click="openUserMessage()">
            <div class="icon">
              <el-icon><Message /></el-icon>
            </div>
            <div class="text">我的消息</div>
          </div>
          <div class="sidebar-wrapper-item" @click="exit()">
            <div class="icon">
              <el-icon><SwitchButton /></el-icon>
            </div>
            <div class="text">退出登录</div>
          </div>
        </div>
      </div>
    </div>
    <div :class="{ 'sidebar-bg': true, 'sidebar-bg-active': isactive }"></div>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance ,onMounted} from "vue";
import { ElNotification, ElMessage } from "element-plus";
import { getCookie, removeCookie } from "@/utils/cookies.js";
import dynamics from "dynamics.js";

const { proxy } = getCurrentInstance();
let url;
let imgurl = ref();
let isactive = ref(false);
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

const buttonActive = () => {
  isactive.value = !isactive.value;
  let items = document.getElementsByClassName("sidebar-wrapper-item");
  if (isactive.value) {
    for (let i = 0; i < items.length; i++) {
      dynamics.animate(
        items[i],
        {
          translateX: -100,
          opacity: 1,
        },
        {
          type: dynamics.easeInOut,
          duration: 500,
          friction: 500,
          delay: i * 150,
        }
      );
    }
  } else {
    for (let i = 0; i < items.length; i++) {
      dynamics.animate(
        items[i],
        {
          translateX: 100,
          opacity: 0,
        },
        {
          type: dynamics.easeInOut,
          duration: 500,
          friction: 500,
          delay: i * 50,
        }
      );
    }
  }
};

const openHome = () => {
  proxy.$router.push({ name: "home" });
};

const openUserInfo = () => {
  if (getCookie("token").token) {
    proxy.$router.push({ name: "user" });
  } else {
    proxy.$router.push({ name: "login" });
  }
};

const openUserArticle = () => {
  if (getCookie("token").token) {
    proxy.$router.push({ name: "user" });
  } else {
    proxy.$router.push({ name: "login" });
  }
};

const openUserMessage = () => {
  if (getCookie("token").token) {
    proxy.$router.push({ name: "message" });
  } else {
    proxy.$router.push({ name: "login" });
  }
};

const openUserCollect = () => {
  if (getCookie("token").token) {
    proxy.$router.push({ name: "user" });
  } else {
    proxy.$router.push({ name: "login" });
  }
};

const exit = () => {
  removeCookie("userName");
  removeCookie("idxUser");
  removeCookie("idxPhoto");
  removeCookie("idxPhotoUrl");
  removeCookie("token");
  proxy.$router.push({ name: "login" });
};
onMounted(() => {

});
</script>
<style scoped>
.out {
  width: 100%;
  height: 7vh;
}
.header-main {
  width: 100%;
  height: 7vh;
  display: flex;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}
.header-main .title {
  width: 20%;
  height: 100%;
  background-image: url("../assets/title-2.png");
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center center;
}
.header-main .button {
  margin-left: 65%;
  padding-right: 5%;
  padding-top: 2vh;
  width: 8%;
  z-index: 4;
}
.header-main .button .line {
  width: 100%;
  height: 0.5vh;
  margin-bottom: 1vh;
  border-radius: 0.5vh;
  background-color: rgba(0, 0, 0, 0.316);
  transition: all 0.5s ease;
}
.header-main .button .firstline {
  margin-top: 1.5vh;
  margin-bottom: -0.5vh;
  background-color: rgba(0, 0, 0, 0.158);
  transform: rotate(45deg);
  transition: all 0.5s ease;
}
.header-main .button .secondline {
  transform: rotate(-45deg);
  transition: all 0.5s ease;
}
.header-main .button .thirdline {
  margin-top: -1.5vh;
  background-color: rgba(0, 0, 0, 0.158);
  transform: rotate(45deg);
  transition: all 0.5s ease;
}
.sidebar {
  top: 0%;
  left: 100%;
  width: 100%;
  height: 100vh;
  position: fixed;
  transition: all 0.5s ease;
}
.sidebar-active {
  z-index: 3;
  left: 30%;
  border-bottom-left-radius: 1vh;
  border-top-left-radius: 1vh;
  background-color: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.5s ease;
}
.sidebar-main {
  padding-left: 8%;
  height: 90vh;
  margin-top: 10vh;
  display: flex;
  flex-direction: column;
}
.sidebar-main .user {
  width: 80%;
  height: 8vh;
  display: flex;
}
.sidebar-main .user .photo {
  width: 8vh;
}
.image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.sidebar-main .user .name {
  margin-left: 8%;
  margin-top: 1vh;
  font-size: 22px;
  font-weight: 500;
  display: flex;
  flex-direction: column;
}
.sidebar-main .sidebar-wrapper {
  width: 80%;
  height: 50vh;
  margin-top: 5vh;
  display: flex;
  flex-direction: column;
}
.sidebar-wrapper-item {
  width: 100%;
  height: 50px;
  opacity: 0;
  margin-left: 20vw;
  padding-left: 5vw;
  margin-bottom: 2vh;
  border-radius: 25px;
  display: flex;
  flex-direction: row;
  cursor: pointer;
  user-select: none;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.sidebar-wrapper-item .icon {
  width: 5vh;
  font-size: 3vh;
  color: brown;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.sidebar-wrapper-item .text {
  font-size: 18px;
  font-weight: 500;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.sidebar-bg {
  top: 0%;
  left: 0%;
  width: 100%;
  height: 100vh;
  position: fixed;
  z-index: -10;
  transition: all 0.5s ease;
}
.sidebar-bg-active {
  z-index: 2;
  background-color: rgba(0, 0, 0, 0.055);
}
</style>
