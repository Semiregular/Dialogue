<template>
  <div class="search-main" @scroll="changeHead($event)">
    <div class="fix-head">
      <div class="search">
        <input
          v-model="key"
          placeholder="请输入搜索内容"
          @keyup.enter="search()"
        />
        <div class="icon" @click="search()">
          <el-icon><Search /></el-icon>
        </div>
      </div>
    </div>
    <div
      class="search-header"
      id="searchHeader"
      @mousemove="headerMove($event)"
    >
      <div class="header-main">
        <div class="title" @click="openHome()"></div>
        <div class="search">
          <input
            v-model="key"
            placeholder="请输入搜索内容"
            @keyup.enter="search()"
          />
          <div class="icon" @click="search()">
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
              <img
                src="../assets/pic-8.jpg"
                class="image"
                v-if="idxPhoto === 0"
              />
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
    </div>
    <div class="type-list">
      <div
        :class="{ item: true, 'type-active': orderType === 0 }"
        @click="changeOrderType(0)"
      >
        默认排序
      </div>
      <div
        :class="{ item: true, 'type-active': orderType === 1 }"
        @click="changeOrderType(1)"
      >
        点赞量排序
      </div>
      <div
        :class="{ item: true, 'type-active': orderType === 2 }"
        @click="changeOrderType(2)"
      >
        收藏量排序
      </div>
      <div
        :class="{ item: true, 'type-active': orderType === 3 }"
        @click="changeOrderType(3)"
      >
        发布时间排序
      </div>
    </div>
    <div class="no-search-list" v-if="iszero && !isload">
      未找到相关内容 o(╥﹏╥)o
    </div>
    <div class="loading" v-if="isload">
      <el-skeleton style="width: 600px" animated>
        <template #template>
          <div
            style="
              display: flex;
              width: 100%;
              margin-left: 10%;
              margin-top: 3vh;
            "
          >
            <el-skeleton-item
              variant="image"
              style="
                width: 180px;
                height: 180px;
                margin-right: 20px;
                border-radius: 8px;
              "
            />
            <div>
              <el-skeleton-item variant="h3" style="width: 350px" />
              <el-skeleton-item variant="text" style="width: 30%" />
              <el-skeleton-item variant="text" style="margin-top: 5vh" />
              <el-skeleton-item variant="text" style="width: 100%" />
              <el-skeleton-item variant="text" style="width: 80%" />
              <el-skeleton-item variant="text" style="width: 100%" />
              <el-skeleton-item variant="text" style="width: 80%" />
              <el-skeleton-item variant="text" style="width: 60%" />
            </div>
          </div>
        </template>
      </el-skeleton>
      <el-skeleton style="width: 600px" animated>
        <template #template>
          <div
            style="
              display: flex;
              width: 100%;
              margin-left: 10%;
              margin-top: 3vh;
            "
          >
            <el-skeleton-item
              variant="image"
              style="
                width: 180px;
                height: 180px;
                margin-right: 20px;
                border-radius: 8px;
              "
            />
            <div>
              <el-skeleton-item variant="h3" style="width: 350px" />
              <el-skeleton-item variant="text" style="width: 30%" />
              <el-skeleton-item variant="text" style="margin-top: 5vh" />
              <el-skeleton-item variant="text" style="width: 100%" />
              <el-skeleton-item variant="text" style="width: 80%" />
              <el-skeleton-item variant="text" style="width: 100%" />
              <el-skeleton-item variant="text" style="width: 80%" />
              <el-skeleton-item variant="text" style="width: 60%" />
            </div>
          </div>
        </template>
      </el-skeleton>
      <el-skeleton style="width: 600px" animated>
        <template #template>
          <div
            style="
              display: flex;
              width: 100%;
              margin-left: 10%;
              margin-top: 3vh;
            "
          >
            <el-skeleton-item
              variant="image"
              style="
                width: 180px;
                height: 180px;
                margin-right: 20px;
                border-radius: 8px;
              "
            />
            <div>
              <el-skeleton-item variant="h3" style="width: 350px" />
              <el-skeleton-item variant="text" style="width: 30%" />
              <el-skeleton-item variant="text" style="margin-top: 5vh" />
              <el-skeleton-item variant="text" style="width: 100%" />
              <el-skeleton-item variant="text" style="width: 80%" />
              <el-skeleton-item variant="text" style="width: 100%" />
              <el-skeleton-item variant="text" style="width: 80%" />
              <el-skeleton-item variant="text" style="width: 60%" />
            </div>
          </div>
        </template>
      </el-skeleton>
      <el-skeleton style="width: 600px" animated>
        <template #template>
          <div
            style="
              display: flex;
              width: 100%;
              margin-left: 10%;
              margin-top: 3vh;
            "
          >
            <el-skeleton-item
              variant="image"
              style="
                width: 180px;
                height: 180px;
                margin-right: 20px;
                border-radius: 8px;
              "
            />
            <div>
              <el-skeleton-item variant="h3" style="width: 350px" />
              <el-skeleton-item variant="text" style="width: 30%" />
              <el-skeleton-item variant="text" style="margin-top: 5vh" />
              <el-skeleton-item variant="text" style="width: 100%" />
              <el-skeleton-item variant="text" style="width: 80%" />
              <el-skeleton-item variant="text" style="width: 100%" />
              <el-skeleton-item variant="text" style="width: 80%" />
              <el-skeleton-item variant="text" style="width: 60%" />
            </div>
          </div>
        </template>
      </el-skeleton>
    </div>
    <div class="search-list" v-if="!iszero && !isload">
      <SearchList
        v-for="item in searchList"
        class="search-list-item"
        :id="item.id"
        :idxUser="item.idxUser"
        :title="item.title"
        :subtitle="item.subtitle"
        :userName="item.userName"
        :summary="item.summary"
        :articleView="item.articleView"
        :articleLike="item.articleLike"
        :articleCollect="item.articleCollect"
        :articlePhotoUrl="item.articlePhotoUrl"
        :userPhotoUrl="item.userPhotoUrl"
        :gmtCreate="item.gmtCreate"
        @click="read(item.id)"
      ></SearchList>
    </div>
    <div class="page" v-if="!iszero && !isload">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="totalPage"
        v-model:current-page="pageCurrent"
        :page-size="pageSize"
        @update:current-page="search()"
      />
    </div>
    <Footer></Footer>
  </div>
</template>
<script setup>
import { storeToRefs } from "pinia";
import { useUserStore } from "@/stores/user";
import { getCookie, removeCookie } from "@/utils/cookies";
import SearchList from "@/components-1/SearchList.vue";
import dynamics from "dynamics.js";
import { ElNotification, ElMessage } from "element-plus";
import { ref, getCurrentInstance, onMounted } from "vue";
import Footer from "@/components-1/Footer.vue";

const userStore = useUserStore();
const { messageUncheck } = storeToRefs(userStore);
const { proxy } = getCurrentInstance();
let isload = ref(true);
let iszero = ref(true);
let isHover = ref(false);
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
let isAsc = ref(0);
let userName = ref(
  getCookie("userName").userName ? getCookie("userName").userName : "未登录"
);
let totalPage = ref(1);
let searchHeader;
let key = ref();
let url;
let searchList = ref([]);
let orderType = ref(0);
let pageCurrent = ref(1);
let pageSize = ref(20);
let blob;
let imgurl = ref();
const changeHover = (state) => {
  isHover.value = state;
};

const changeOrderType = (type) => {
  if (orderType.value === type) {
    if (isAsc.value === 0) {
      isAsc.value = 1;
    } else {
      isAsc.value = 0;
    }
  } else {
    orderType.value = type;
    isAsc.value = 0;
  }
  search();
};

const search = () => {
  if (key.value) {
    isload.value = true;
    iszero.value = true;
    searchList.value = [];
    proxy.$router.push({
      name: "search",
      query: {
        key: encodeURIComponent(key.value),
        orderType: 1,
        pageCurrent: 1,
        pageSize: 20,
      },
    });
    url = `/article/search/mysql?key=${key.value}&pageCurrent=${
      pageCurrent.value
    }&pageSize=${pageSize.value}&orderType=${
      orderType.value + 4 * isAsc.value
    }`;
    proxy.$axios
      .get(url)
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          searchList.value = response.data.data.record;
          totalPage.value = response.data.data.total;
          if (searchList.value.length) {
            setTimeout(() => {
              isload.value = false;
            }, 300);
            iszero.value = false;
            setTimeout(() => {
              let items = document.getElementsByClassName("search-list-item");
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
            }, 600);
          } else {
            setTimeout(() => {
              isload.value = false;
            }, 300);
            iszero.value = true;
          }
        }
      })
      .catch((error) => {
      });
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

const computedBgPosition = (num, doc) => {
  return 60 - Number(((num / doc) * 20).toFixed(1)) + "%";
};

const headerMove = (e) => {
  searchHeader = document.getElementById("searchHeader");
  searchHeader.style.backgroundPosition = `
    ${computedBgPosition(e.clientX, window.innerWidth)}
    0
  `;
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

const openHome = () => {
  proxy.$router.push({ name: "home" });
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

const read = (id) => {
  let readRoute = proxy.$router.resolve({
    name: "read",
    query: {
      idxArticle: id,
    },
  });
  window.open(readRoute.href, "_blank");
};

const changeHead = (e) => {
  let searchMain = document.getElementsByClassName("search-main")[0];
  let scrollTop = searchMain.scrollTop;
  if (scrollTop >= 150) {
    let fixHead = document.getElementsByClassName("fix-head")[0];
    fixHead.style.display = "flex";
  } else if (scrollTop < 150) {
    let fixHead = document.getElementsByClassName("fix-head")[0];
    fixHead.style.display = "none";
  }
};

onMounted(() => {
  key.value = decodeURIComponent(proxy.$route.query.key);
  url = `/article/search/mysql?key=${key.value}&pageCurrent=1&pageSize=20&orderType=0`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        searchList.value = response.data.data.record;
        totalPage.value = response.data.data.total;
        if (searchList.value.length) {
          iszero.value = false;
          setTimeout(() => {
            isload.value = false;
          }, 300);
          setTimeout(() => {
            let items = document.getElementsByClassName("search-list-item");
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
          }, 600);
        } else {
          setTimeout(() => {
            isload.value = false;
          }, 300);
          iszero.value = true;
        }
      }
    })
    .catch((error) => {

    });
});
</script>
<style scoped>
.search-main {
  width: 100%;
  height: 100vh;
  overflow-x: hidden;
}
.search-main .search-header {
  width: 100%;
  height: 20vh;
  background-image: url("../assets/pic-8.jpg");
  background-position: center top;
  background-repeat: no-repeat;
  background-size: 110% 220%;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
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
  background-image: url("../assets/title-2.png");
  background-position: center center;
  background-repeat: no-repeat;
  background-size: contain;
  filter: drop-shadow(1px 1px 1px rgb(0, 0, 0, 0.4));
  cursor: pointer;
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
  background-color: rgb(239, 236, 236);
  transition: all 0.2s ease;
}
.header-main .search input:hover {
  background-color: rgb(245, 245, 245);
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
  background-color: rgb(239, 236, 236);
  transition: all 0.2s ease;
}
.header-main .search .icon:hover {
  background-color: rgb(245, 245, 245);
  transition: all 0.2s ease;
}
.header-main .tool {
  margin-left: 10%;
  display: flex;
}
.header-main .tool .photo {
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
  user-select: none;
}
.image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
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
.search-main .search-list {
  padding: 20px;
  height: auto;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
}
.type-list {
  width: 100%;
  height: 4vh;
  display: flex;
  flex-direction: row;
  margin-top: 6vh;
  margin-left: 10vh;
  font-size: 18px;
  font-weight: 500;
}
.type-list .item {
  margin-right: 25px;
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;
  user-select: none;
}
.type-list .item:hover {
  color: rgb(214, 31, 31);
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
}
.type-list .type-active {
  color: rgb(214, 31, 31);
  border-bottom: 2px solid rgb(214, 31, 31);
  transition: all 0.2s ease;
}
.user-info {
  width: 5vh;
  padding-left: 3vh;
  height: 8vh;
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
.page {
  margin-left: 8vh;
  margin-bottom: 3vh;
}
.no-search-list {
  width: 100%;
  height: 50vh;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 20px;
  font-weight: 600;
  user-select: none;
}
.loading {
  width: 100%;
  height: 50vh;
  display: flex;
  flex-wrap: wrap;
}
:deep(.el-pagination.is-background .el-pager li.is-active) {
  background-color: rgb(214, 31, 31);
}
.fix-head {
  position: fixed;
  z-index: 10;
  width: calc(100vw - 16px);
  display: none;
  height: 8vh;
  background-color: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.fix-head .search {
  margin-left: 30%;
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
  text-indent: 12px;
  font-size: 18px;
  font-weight: 500;
  border-radius: 18px;
  background-color: rgb(239, 236, 236);
  transition: all 0.2s ease;
}
.fix-head .search input:hover {
  background-color: rgb(245, 245, 245);
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
  background-color: rgb(239, 236, 236);
  transition: all 0.2s ease;
}
.fix-head .search .icon:hover {
  background-color: rgb(245, 245, 245);
  transition: all 0.2s ease;
}

:deep(.el-badge__content--danger) {
  background-color: rgb(214, 31, 31);
}
:deep(.el-badge__content) {
  border: none;
}
</style>
