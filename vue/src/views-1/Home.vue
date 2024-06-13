<template>
  <div class="main">
    <div class="load" @mousemove="move($event)">
      <div :class="{ title: true, 'title-active': isload }">对话</div>
      <div :class="{ button: true, 'button-active': isload }" @click="enter()">
        進入首頁
      </div>
      <!-- <div :class="{ pic: true, 'pic-active': isload }"></div> -->
      <div :class="{ video: true, 'video-active': isload }">
        <video
          src="https://dialogue-oss.oss-cn-wulanchabu.aliyuncs.com/backgroundVideo.mp4"
          type="video/mp4"
          autoplay="true"
          muted="true"
          loop="true"
          height="1080px"
          width="1920px"
        >
          您的浏览器不支持 video 标签
        </video>
      </div>
    </div>
    <div
      :class="{ service: true, 'service-active': isload }"
      @scroll="changeHead($event)"
    >
      <div class="home-header" id="homeHeader" @mousemove="headerMove($event)">
        <Header></Header>
      </div>
      <div class="fix-head">
        <div class="title" @click="openHome()"></div>
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
          <div class="photo" @click="openUserInfo()">
            <img :src="idxPhotoUrl" class="image" v-if="idxPhotoUrl !== 0" />
            <img
              src="../assets/pic-8.jpg"
              class="image"
              v-if="idxPhotoUrl === 0"
            />
          </div>
          <div class="item" @click="openUserMessage()">
            <div class="icon">
              <el-badge
                :value="messageUncheck"
                :max="9"
                class="top"
                v-if="messageUncheck !== 0"
              >
                <el-icon><Message /></el-icon>
              </el-badge>
              <el-icon v-if="messageUncheck === 0"><Message /></el-icon>
            </div>
            <div class="text">消息</div>
          </div>
          <div class="item" @click="openEdit()">
            <div class="icon">
              <el-icon><Edit /></el-icon>
            </div>
            <div class="text">投稿</div>
          </div>
        </div>
      </div>
      <div class="module-nav">
        <div v-for="item in module" class="module-nav-item" @click="openAct()">
          {{ item }}
        </div>
      </div>
      <div class="article-wrapper">
        <div class="article-top-bg">
          <el-carousel style="min-width: 38vw" height="48vh">
            <el-carousel-item v-for="item in 4" :key="item">
              <div class="article-top">
                <div class="top-title">&nbsp;&nbsp;对话平台公告</div>
                <div class="top-summary">
                  &nbsp;&nbsp;&nbsp;&nbsp;求知之人，见证智慧；诓惑之人，放下过往；羁旅之人，寻觅归途；修行之人，成就自我。
                  “对话”——哲思类交流平台为第八届雏雁计划项目，致力于打造一个给人哲思，启发哲思的网络自留地，
                  让大众真正走向哲学，理解哲学，热爱哲学。<br />
                  &nbsp;&nbsp;&nbsp;&nbsp;我们会对哲思类书籍进行广泛阅读，并做详尽的注解，方便大家交流阅读，营造良好的阅读环境。
                  我们会针对主要的哲学门类，并结合中国特有的本土宗教和哲学，为大家提供一种注解。
                  同时，我们也会从他人的想法中提炼，完成论坛的初步阶段。<br />
                  &nbsp;&nbsp;&nbsp;&nbsp;欢迎大家的使用，谢谢！
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
        <dic class="article-list">
          <HomeList
            v-for="item in article"
            class="article-list-item"
            :title="item.title"
            :subtitle="item.subtitle"
            :userName="item.userName"
            :summary="item.summary"
            :articleView="item.articleView"
            :articleLike="item.articleLike"
            :articlePhotoUrl="item.articlePhotoUrl"
            :userPhotoUrl="item.userPhotoUrl"
            :gmtCreate="item.gmtCreate"
            @click="read(item.id)"
          ></HomeList>
        </dic>
      </div>
      <div class="activity">
        <div class="middle">
          <div class="left">
            <div class="note">本月话题</div>
            <div class="ani">
              <div class="cir cir-1"><div class="text">毛选</div></div>
              <div class="cir cir-2"><div class="text">红色影视</div></div>
              <div class="cir cir-3" @click="openAct()"></div>
              <div class="cir cir-4"><div class="text">改革开放</div></div>
            </div>
          </div>
          <div class="rank">
            <div class="note">排行</div>
            <div v-for="(item, index) in ranklist" class="rank-item">
              <div class="num">{{ index + 1 }}</div>
              <div class="title">{{ item }}</div>
            </div>
          </div>
        </div>
      </div>
      <Footer></Footer>
    </div>
  </div>
</template>
<script setup>
import { getCookie } from "@/utils/cookies";
import Header from "@/components-1/HeaderHome.vue";
import Footer from "@/components-1/Footer.vue";
import HomeList from "@/components-1/HomeList.vue";
import { ElNotification, ElMessage } from "element-plus";
import dynamics from "dynamics.js";
import { ref, getCurrentInstance, onMounted } from "vue";
import { storeToRefs } from "pinia";
import { useUserStore } from "@/stores/user";
const userStore = useUserStore();
const { messageUncheck } = storeToRefs(userStore);
const { proxy } = getCurrentInstance();
let module = ref([
  "红色经典",
  "民国传记",
  "西方哲学",
  "百家诸子",
  "佛道古籍",
  "近现代散文",
  "个人专栏",
  "精品推荐",
]);
let ranklist = ref([
  "xxxxxx",
  "xxxxxx",
  "xxxxxx",
  "xxxxxx",
  "xxxxxx",
  "xxxxxx",
]);
let url = "";
let article = ref([]);
let isload = ref(getCookie("token").token ? true : false);
let pic;
let title;
let button;
let load;
let homeHeader;
let key = ref("禅师");
let idxPhotoUrl = ref(
  getCookie("idxPhotoUrl").idxPhotoUrl
    ? getCookie("idxPhotoUrl").idxPhotoUrl
    : 0
);
let idxUser = ref(
  Number(getCookie("idxUser").idxUser)
    ? Number(getCookie("idxUser").idxUser)
    : 0
);

if (isload.value) {
  setTimeout(() => {
    load = document.querySelector(".load");
    load.style.display = "none";
  }, 1000);
}

const enter = () => {
  isload.value = !isload.value;
  setTimeout(() => {
    load = document.querySelector(".load");
    load.style.display = "none";
  }, 1000);
};

const computedTransformX = (num, doc) => {
  return 10 - ((num / doc) * 50).toFixed(1);
};

const computedTransformY = (num, doc) => {
  return 10 - ((num / doc) * 10).toFixed(1);
};

const computedRotate = (num, doc) => {
  return ((num / doc) * (num / doc)).toFixed(1);
};

const computedBgPosition = (num, doc) => {
  return 60 - Number(((num / doc) * 20).toFixed(1)) + "%";
};

const move = (e) => {
  //pic = document.querySelector(".pic");
  title = document.querySelector(".title");
  button = document.querySelector(".button");
  // pic.style.transform = `
  //   rotateX(${computedRotate(e.clientX, window.innerWidth)}deg)
  //   rotateY(${computedRotate(e.clientY, window.innerHeight)}deg)
  // `;
  // pic.style.backgroundPosition = `
  //   ${computedBgPosition(e.clientX, window.innerWidth)}
  //   ${computedBgPosition(e.clientY, window.innerHeight)}
  // `;
  if (title !== undefined) {
    title.style.transform = `
    translateX(${computedTransformX(e.clientX, window.innerWidth)}px)
    translateY(${computedTransformY(e.clientY, window.innerHeight)}px)
  `;
  }
  if (button !== undefined) {
    button.style.transform = `
    translateX(${computedTransformX(e.clientX, window.innerWidth)}px)
    translateY(${computedTransformY(e.clientY, window.innerHeight)}px)
  `;
  }
};

const headerMove = (e) => {
  homeHeader = document.getElementById("homeHeader");
  if (homeHeader !== undefined) {
    homeHeader.style.backgroundPosition = `
    ${computedBgPosition(e.clientX, window.innerWidth)} 
    0
  `;
  }
};

const read = (id) => {
  let readRoute = proxy.$router.resolve({
    name: "read",
    query: {
      idxArticle: id,
    },
  });
  window.open(readRoute.href, "_blank");
};

const openAct = () => {
  let activityRoute = proxy.$router.resolve({
    name: "activity",
    query: {
      id: 1,
    },
  });
  window.open(activityRoute.href, "_blank");
};

const changeHead = (e) => {
  let service = document.getElementsByClassName("service")[0];
  let scrollTop = service.scrollTop;
  if (scrollTop >= 150) {
    let fixHead = document.getElementsByClassName("fix-head")[0];
    fixHead.style.display = "flex";
  } else if (scrollTop < 150) {
    let fixHead = document.getElementsByClassName("fix-head")[0];
    fixHead.style.display = "none";
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

onMounted(() => {
  url = "/article/search/mysql?key=1&pageCurrent=1&pageSize=4&orderType=0";
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        article.value = response.data.data.record;
        setTimeout(() => {
          let items = document.getElementsByClassName("article-list-item");
          for (let i = 0; i < items.length; i++) {
            dynamics.animate(
              items[i],
              {
                translateX: 10,
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
        }, 300);
      }
    })
    .catch((error) => {});
});
</script>
<style scoped>
@font-face {
  font-family: mao;
  src: url("https://dialogue-oss.oss-cn-wulanchabu.aliyuncs.com/maozedong.ttf");
}
.main {
  width: 100%;
  height: 100vh;
  overflow-x: hidden;
}
.main .load {
  top: 0%;
  left: 0%;
  width: 100%;
  height: 100%;
  position: fixed;
  z-index: 2;
  display: flex;
  flex-direction: column;
  user-select: none;
}
.main .load .title {
  width: 60%;
  height: 30%;
  margin-top: 5%;
  margin-left: 20%;
  font-family: mao;
  text-align: center;
  font-weight: 500;
  font-size: 280px;
  color: white;
  filter: drop-shadow(1px 1px 5px rgba(0, 0, 0, 0.65));
  transition: all 0.5s ease;
}
.main .load .title-active {
  opacity: 0;
  margin-left: 10%;
  transition: all 0.5s ease;
}
.main .load .button {
  left: 43%;
  top: 70%;
  position: absolute;
  width: 16%;
  height: 4%;
  color: #1c93ce;
  text-align: center;
  font-size: 20px;
  font-weight: 600;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: white;
  border: 0px solid rgb(214, 31, 31);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  border-radius: 15px;
  cursor: pointer;
  filter: drop-shadow(1px 1px 2px rgba(0, 0, 0, 0.3));
  transition: all 0.5s ease;
}
.main .load .button-active {
  left: 33%;
  opacity: 0;
  z-index: -2;
  transition: all 0.5s ease;
}
.main .load .pic {
  position: fixed;
  top: 0%;
  left: 0%;
  width: 100%;
  height: 100%;
  opacity: 0.5;
  background-image: url("../assets/pic-8.jpg");
  background-position: center center;
  background-repeat: no-repeat;
  background-size: 110% 110%;
  z-index: -1;
  transition: all 0.5s ease;
}
.main .load .pic-active {
  left: -20%;
  opacity: 0;
  transition: all 0.5s ease;
}
.main .video {
  position: fixed;
  top: 0%;
  left: 0%;
  width: 100%;
  height: 100%;
  z-index: -1;
  transition: all 0.5s ease;
}
.main .video-active {
  left: -20%;
  opacity: 0;
  transition: all 0.5s ease;
}

.main .service {
  width: 100%;
  height: 100vh;
  margin-left: 30%;
  overflow-y: hidden;
  opacity: 0;
  z-index: -3;
  transition: all 0.5s ease;
}
.main .service-active {
  opacity: 1;
  overflow-y: auto;
  margin-left: 0%;
  z-index: 0;
  transition: all 0.5s ease;
}
.main .home-header {
  width: 100%;
  height: 20vh;
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
}
.main .article-wrapper {
  width: 95%;
  height: 50vh;
  overflow: hidden;
  margin-top: 8vh;
  margin-left: 2.5%;
  margin-right: 2.5%;
  display: flex;
}
.main .article-wrapper .article-top-bg {
  width: 40%;
  height: 90%;
  padding: 1vh;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  border: 8px solid #ffd7c9;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.main .article-wrapper .article-top {
  width: 95%;
  height: 95%;
  padding: 12px;
  border-radius: 8px;
  margin-top: 0%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: left;
  background-color: white;
}
.main .article-wrapper .article-top .top-title {
  height: 15%;
  font-weight: 600;
  font-size: 22px;
  user-select: none;
}
.main .article-wrapper .article-top .top-summary {
  height: 75%;
  font-size: 18px;
  font-family: "仿宋";
  line-height: 25px;
}
.main .article-wrapper .article-list {
  margin-left: 2%;
  width: 58%;
  height: 100%;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  align-content: flex-start;
  z-index: 1;
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
.fix-head {
  width: calc(100vw - 16px);
  height: 8vh;
  display: none;
  position: fixed;
  top: 0%;
  left: 0%;
  z-index: 10;
  background-color: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.fix-head .title {
  width: 10%;
  background-image: url("../assets/title-2.png");
  background-position: center center;
  background-repeat: no-repeat;
  background-size: contain;
  filter: drop-shadow(1px 1px 1px rgb(0, 0, 0, 0.4));
  cursor: pointer;
}
.fix-head .search {
  margin-left: 20%;
  margin-top: 1vh;
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
.fix-head .search input {
  outline: none;
  width: 95%;
  border: 0;
  text-indent: 0.5em;
  font-size: 18px;
  font-weight: 500;
  border-radius: 18px;
  background-color: rgba(255,255,255,0.9);
  transition: all 0.2s ease;
}
.fix-head .search input:hover {
  background-color: white;
  transition: all 0.2s ease;
}
.fix-head .search .icon {
  padding: 1vh;
  border-radius: 50%;
  margin-left: 6px;
  width: 3vh;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 22px;
  background-color: rgba(255,255,255,0.9);
  transition: all 0.2s ease;
}
.fix-head .search .icon:hover {
  background-color: white;
  transition: all 0.2s ease;
}
.fix-head .tool {
  margin-left: 10%;
  display: flex;
}
.fix-head .tool .photo {
  width: 5vh;
  height: 5vh;
  max-width: 6vh;
  cursor: pointer;
  margin-top: 1.5vh;
  margin-bottom: 1.5vh;
  margin-right: 1vh;
  border-radius: 50%;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.5s ease;
}
.fix-head .tool .photo .image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.fix-head .tool .photo:hover {
  width: 6vh;
  height: 6vh;
  margin-top: 1vh;
  margin-bottom: 1vh;
  margin-right: 0vh;
}
.fix-head .tool .item {
  margin-left: 18px;
  user-select: none;
  display: flex;
  cursor: pointer;
  text-align: center;
  flex-direction: column;
  justify-content: center;
}
.fix-head .tool .item .icon {
  color: rgb(214, 31, 31);
  font-size: 28px;
  font-weight: 500;
  filter: drop-shadow(1px 1px 1px rgb(0, 0, 0, 0.2));
  transition: all 0.5s ease;
}
.fix-head .tool .item .icon:hover {
  animation-name: icon;
  animation-duration: 0.3s;
  animation-timing-function: ease;
  animation-iteration-count: 1;
}
.fix-head .tool .item .text {
  margin-top: -6px;
  font-size: 12px;
  font-weight: 400;
}

:deep(.el-badge__content--danger) {
  background-color: rgb(214, 31, 31);
}
:deep(.el-badge__content) {
  border: none;
}
.module-nav {
  width: 95%;
  margin-left: 2.5%;
  margin-top: 3vh;
  height: 50px;
  display: flex;
  justify-content: space-around;
  align-content: flex-start;
  align-items: center;
  border-radius: 25px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.module-nav-item {
  border-radius: 18px;
  width: 10%;
  height: 36px;
  font-weight: 600;
  font-size: 16px;
  margin-left: 1.25%;
  margin-right: 1.25%;
  box-shadow: rgba(0, 4px, 16px, 0.1);
  text-align: center;
  display: flex;
  color: rgb(214, 31, 31);
  justify-content: center;
  flex-direction: column;
  user-select: none;
  cursor: pointer;
  transition: all 0.2s ease;
}
.module-nav-item:hover {
  box-shadow: rgba(0, 4px, 16px, 0.15);
  background-color: #fde2e2;
  transition: all 0.1s ease;
}
.activity {
  width: 95%;
  height: 52vh;
  margin-top: 8vh;
  margin-left: 2.5%;
  margin-right: 2.5%;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
  border-top-right-radius: 10%;
  border-bottom-right-radius: 10%;
}
.activity .note {
  width: 200px;
  height: 30px;
  padding-left: 20px;
  padding-top: 20px;
  padding-bottom: 10px;
  font-size: 22px;
  font-weight: 600;
  color: rgb(214, 31, 31);
  user-select: none;
  cursor: pointer;
}
.rank .note {
  padding-left: 10px;
}
.activity .middle {
  display: flex;
  width: calc(100% - 10px);
  padding-right: 10px;
  margin-left: 10px;
  height: 100%;
}
.activity .left {
  width: calc(75% - 20px);
  height: 100%;
  margin-right: 10px;
}
.left .ani {
  width: 100%;
  height: calc(100% - 60px);
  display: flex;
}
.activity .rank {
  width: 25%;
  padding-left: 10px;
  height: 100%;
  border-top-right-radius: 10%;
  border-bottom-right-radius: 10%;
}
.cir-1 {
  width: 100px;
  height: 100px;
  top: 100px;
  left: 30px;
  background-color: #66ccff;
}
.cir-1:hover {
  width: 110px;
  height: 110px;
  top: 95px;
  left: 25px;
  margin-right: -10px;
  background-color: #66ccff;
}
.cir-2 {
  width: 150px;
  height: 150px;
  top: calc(100% - 200px);
  left: 60px;
  background-color: #66ccff;
}
.cir-2:hover {
  width: 160px;
  height: 160px;
  top: calc(100% - 205px);
  left: 55px;
  margin-right: -10px;
  background-color: #66ccff;
}
.cir-4 {
  width: 180px;
  height: 180px;
  top: calc(50% - 140px);
  left: calc(30% - 50px);
  background-color: #66ccff;
  transition: all 0.1s ease;
}
.cir-4:hover {
  width: 190px;
  height: 190px;
  top: calc(50% - 145px);
  left: calc(30% - 55px);
  transition: all 0.1s ease;
}
.cir-3 {
  width: 280px;
  height: 280px;
  top: calc(50% - 140px);
  left: calc(30% - 140px);
  background-color: #f8dcdc;
}
.cir-3:hover::before {
  background-color: #d03f09;
  width: 250px;
  height: 250px;
  line-height: 250px;
  margin-left: 15px;
  cursor: pointer;
  transition: all 0.1s ease;
}
.cir-3::after {
  content: "";
  width: 310px;
  height: 310px;
  margin-left: -15px;
  border-radius: 50%;
  display: inline-block;
  position: absolute;
  background-color: #f7e8e8;
  z-index: -1;
}
.cir-3::before {
  content: "红色经典";
  color: white;
  font-weight: 600;
  font-size: 30px;
  text-align: center;
  line-height: 240px;
  font-family: mao;
  width: 240px;
  height: 240px;
  margin-left: 20px;
  border-radius: 50%;
  display: inline-block;
  position: absolute;
  background-color: #e37d7d;
  z-index: 1;
  transition: all 0.1s ease;
}
.cir {
  border-radius: 50%;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.cir .text {
  text-align: center;
  color: white;
  font-size: 20px;
  font-weight: 600;
  user-select: none;
  font-family: mao;
}
.rank {
  background: linear-gradient(
    90deg,
    #ffc9c9,
    #ffcdc8,
    #ffd2c8,
    #ffd7c9,
    #ffdcca,
    #ffe0cc,
    #ffe5cf,
    #ffead3
  );
}
.rank-item {
  padding-left: 10px;
  padding-right: 10px;
  max-width: calc(100% - 20px);
  min-width: 60%;
  width: fit-content;
  height: 34px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 5px;
  user-select: none;
  cursor: pointer;
  display: flex;
}
.rank-item:hover {
  background-color: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}
.rank-item .num {
  display: flex;
  flex-direction: column;
  justify-content: center;
  line-height: 34px;
  height: 34px;
  color: rgb(214, 31, 31);
  font-weight: 600;
  margin-right: 10px;
}
.rank-item .title {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
:deep(.el-carousel__button){
  background-color:rgb(214, 31, 31);
}
</style>
