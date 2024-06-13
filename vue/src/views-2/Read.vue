<template>
  <div class="read-main">
    <Header></Header>
    <div class="wrapper">
      <div class="middle">
        <div class="title">
          <div class="main">{{ title }}</div>
          <div class="sub">{{ subtitle }}</div>
        </div>
        <div class="info">
          <div class="item">发布时间：{{ gmtCreate }}</div>
          <div class="data">
            <div class="item">浏览量：{{ articleView }}</div>
            <div class="item">点赞量：{{ articleLike }}</div>
            <div class="item">收藏量：{{ articleCollect }}</div>
          </div>
        </div>
        <div class="author">
          <div class="photo">
            <img
              :src="authorPhotoUrl"
              class="user-image"
            />
          </div>
          <div class="name">{{ authorName }}</div>
        </div>
        <div class="summary">摘要：{{ summary }}</div>
        <div class="article-photo">
          <img :src="articlePhotoUrl" class="image"  />
        </div>
        <div class="content" v-if="editType === 2" v-html="content"></div>
        <div class="content" v-if="editType === 1">
          <Viewer :value="content"></Viewer>
        </div>
        <el-divider />
        <div class="other">
          <div class="tag">
            <div class="pre">标签：</div>
            <div v-for="item in tag" class="tag-item">{{ item.tag }}</div>
          </div>
          <div class="item">修改于：{{ gmtModify }}</div>
        </div>
      </div>
      <div class="comment">
        <div class="comment-title">评论 {{ comment.length }}</div>
        <div class="no-comment" v-if="iszero">还没有评论 o(╥﹏╥)o</div>
        <div class="comment-list" v-if="!iszero">
          <Comment
            v-for="item in comment"
            class="comment-list-item"
            :id="item.id"
            :idxAuthor="item.idxUser"
            :authorName="item.userName"
            :content="item.content"
            :commentLike="item.commentLike"
            :userPhotoUrl="item.userPhotoUrl"
            :sublist="item.sublist"
            :gmtCreate="item.gmtCreate"
            :isLike="item.isLike"
          ></Comment>
        </div>
        <div :class="{ mycomment: true, 'mycomment-active': isClick }">
          <div class="top">
            <div class="left">
              <img :src="idxPhotoUrl" class="image" v-if="idxPhoto !== 0" />
              <img
                src="../assets/pic-8.jpg"
                class="image"
                v-if="idxPhoto === 0"
              />
            </div>
            <div class="right">
              <textarea
                placeholder="期待你的精彩评论！(*^▽^*)"
                v-model="myComment"
                @input="changeInput"
                @click="changeClick(true)"
                @blur="changeClick(false)"
                :class="{ 'text-active': isInput }"
              ></textarea>
            </div>
          </div>
          <div
            class="button"
            @click="submit()"
            @mouseenter="changeEnter(true)"
            @mouseleave="changeEnter(false)"
          >
            发布
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import Header from "@/components-2/Header.vue";
import { ElNotification, ElMessage } from "element-plus";
import { storeToRefs } from "pinia";
import { Viewer } from "@bytemd/vue-next";
import { useUserStore } from "@/stores/user";
import { getDate } from "@/utils/getDate";
import { getCookie } from "@/utils/cookies";
import Comment from "@/components-2/CommentMain.vue";
import { ref, getCurrentInstance, onMounted } from "vue";
const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const { ip } = storeToRefs(userStore);

let iszero = ref(true);
let url;
let tag = ref([]);
let editType = ref(1);
let idxArticle = ref();
let title = ref();
let subtitle = ref();
let authorName = ref();
let idxAuthor = ref();
let content = ref();
let gmtCreate = ref();
let gmtModify = ref();
let comment = ref([]);
let summary = ref();
let articleCollect = ref();
let articleLike = ref();
let articleView = ref();
let articlePhotoUrl = ref(0);
let isLike = ref(false);
let isCollect = ref(false);
let userimgurl = ref();
let authorPhotoUrl = ref(0);
let isInput = ref(false);
let isClick = ref(false);
let isEnter = ref(false);
let myComment = ref();
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
let idxUser = ref(
  Number(getCookie("idxUser").idxUser)
    ? Number(getCookie("idxUser").idxUser)
    : 0
);
let userName = ref(
  getCookie("userName").userName ? getCookie("userName").userName : "未登录"
);

const changeInput = (e) => {
  if (myComment.value) {
    isInput.value = true;
  } else {
    isInput.value = false;
  }
};
const changeClick = (state) => {
  if (!isEnter.value) {
    isClick.value = state;
  }
};

const submit = () => {
  if (myComment.value) {
    proxy.$axios
      .post("/comment/insert", {
        idxArticle: idxArticle.value,
        idxUser: idxUser.value,
        idxComment: 0,
        ip: ip.value[0],
        userName: userName.value,
        userPhoto: idxPhoto.value,
        content: myComment.value,
        gmtCreate: getDate(),
        gmtModify: getDate(),
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          myComment.value = "";
          isClick.value = false;
          ElMessage({
            showClose: false,
            message: "提交成功",
            type: "success",
            duration: 1000,
            center: true,
            grouping: false,
          });
          getMainComment();
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
  } else {
    ElMessage({
      showClose: false,
      message: "评论内容不能为空",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: false,
    });
  }
  isClick.value = false;
};

const getMainComment = () => {
  url = `/comment/get/main?idxArticle=${idxArticle.value}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        comment.value = response.data.data;
        if (comment.value.length) {
          iszero.value = false;
        } else {
          iszero.value = true;
        }
      }
    })
    .catch((error) => {

    });
};

const scrollComment = () => {
  let comment = document.getElementsByClassName("comment")[0];
  comment.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
};

document.body.onscroll = () => {
  let scrollTop = document.documentElement.scrollTop;
  let comment = document.getElementsByClassName("comment")[0];
  let mycomment = document.getElementsByClassName("mycomment")[0];
  if (
    scrollTop + window.innerHeight >= comment.offsetTop + 200 ||
    isClick.value
  ) {
    mycomment.style.display = "block";
  } else {
    mycomment.style.display = "none";
  }
};
const changeEnter = (state) => {
  isEnter.value = state;
};

onMounted(() => {
  idxArticle.value = decodeURIComponent(proxy.$route.query.idxArticle);

  url = `/article/get/read?idxArticle=${idxArticle.value}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        idxAuthor.value = response.data.data.idxUser;
        authorName.value = response.data.data.userName;
        title.value = response.data.data.title;
        subtitle.value = response.data.data.subtitle;
        summary.value = response.data.data.summary;
        articleView.value = response.data.data.articleView;
        articleLike.value = response.data.data.articleLike;
        articleCollect.value = response.data.data.articleCollect;
        articlePhotoUrl.value = response.data.data.articlePhotoUrl;
        authorPhotoUrl.value = response.data.data.userPhotoUrl;
        content.value = response.data.data.content;
        gmtCreate.value = response.data.data.gmtCreate;
        gmtModify.value = response.data.data.gmtModify;
        editType.value = response.data.data.editType;
        tag.value = response.data.data.tagList;
       
      }
    })
    .catch((error) => {

    });


  url = `/comment/get/main?idxArticle=${idxArticle.value}&idxUser=${idxUser.value}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        comment.value = response.data.data.record;
        if (comment.value.length) {
          iszero.value = false;
        } else {
          iszero.value = true;
        }
      }
    })
    .catch((error) => {

    });
});
</script>
<style scoped>
.read-main {
  width: 100%;
  height: 100vh;
}
.read-main .wrapper {
  width: 100vw;
  display: flex;
  border-top: 2px solid #eee;
  flex-direction: column;
  align-items: center;
  min-height: 92vh;
  justify-content: center;
  background-color: rgba(97, 97, 97, 0.05);
}
.read-main .wrapper .middle {
  display: flex;
  flex-direction: column;
  width: 90%;
  padding-top: 3vh;
  padding-left: 5%;
  padding-right: 5%;
  background-color: white;
}
.read-main .middle .title {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-top: 1vh;
}
.read-main .middle .title .main {
  font-size: 22px;
  font-weight: 600;
}
.read-main .middle .title .sub {
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 18px;
  font-weight: 500;
}
.read-main .middle .info {
  user-select: none;
  margin-top: 1vh;
  margin-bottom: 3vh;
  justify-content: space-between;
  flex-direction: row;
  color: rgba(0, 0, 0, 0.5);
  font-size: 14px;
}
.read-main .middle .info .data {
  display: flex;
  margin-top: 1vh;
}
.info .data .item {
  margin-right: 10px;
  font-size: 14px;
}
.read-main .middle .author {
  display: flex;
  flex-direction: row;
  margin-bottom: 3vh;
}
.read-main .middle .author .photo {
  width: 5vh;
  height: 5vh;
  border-radius: 50%;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.image {
  width: 100%;
  height: 100%;
}
.user-image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.read-main .middle .author .name {
  font-size: 16px;
  font-weight: 500;
  margin-left: 10px;
  user-select: none;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.read-main .middle .summary {
  font-size: 16px;
  font-weight: 500;
}
.read-main .middle .article-photo {
  margin-top: 3vh;
  margin-bottom: 3vh;
  width: 100%;
  height: 20vh;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.article-photo .image {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.read-main .middle .content {
  font-size: 20px;
  font-weight: 400;
  line-height: 30px;
  font-family: "仿宋";
  white-space: pre-wrap;
  word-wrap: break-word;
}
.read-main .middle .other {
  display: flex;
  justify-content: space-between;
  color: rgba(0, 0, 0, 0.5);
  font-size: 14px;
  user-select: none;
}
.read-main .tag {
  display: flex;
  user-select: none;
}
.tag-item {
  margin-right: 10px;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
}
.tag-item:hover {
  color: rgb(214, 31, 31);
  transition: all 0.2s ease;
}
.read-main .comment {
  width: 90%;
  padding: 5%;
  min-height: 20vh;
  margin-bottom: 10vh;
  padding-top: 5vh;
  background-color: white;
}
.no-comment {
  width: 100%;
  height: 20vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  user-select: none;
  font-size: 16px;
  color: rgba(0, 0, 0, 0.5);
  font-weight: 500;
}
.read-main .comment .comment-title {
  font-size: 18px;
  font-weight: 500;
}
.read-main .comment .comment-list {
  margin-top: 5vh;
  display: flex;
  flex-direction: column;
}
.right-side {
  position: fixed;
  top: 60%;
  left: 85%;
  width: 5%;
  background-color: white;
  height: 20vh;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  display: flex;
  border-radius: 8px;
  flex-direction: column;
  padding-top: 1vh;
  padding-bottom: 1vh;
}
.right-side .item {
  width: 100%;
  height: 7vh;
  text-align: center;
  margin-bottom: 1.5vh;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: all 0.2s ease;
}
.right-side .item .icon {
  font-size: 25px;
}
.right-side .item:hover {
  color: rgb(214, 31, 31);
  transition: all 0.2s ease;
}
.right-side .item .data {
  margin-top: -5px;
}
.right-side .opt-active {
  color: rgb(214, 31, 31);
  transition: all 0.2s ease;
}
.mycomment {
  position: fixed;
  display: none;
  width: 90vw;
  padding-left: 5vw;
  padding-right: 5vw;
  left: 0vw;
  top: 90vh;
  height: 15vh;
  border-radius: 10px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  background-color: white;
  transition: all 0.5s ease;
}
.mycomment-active {
  top: 85vh;
  transition: all 0.5s ease;
}
.mycomment .top {
  width: 100%;
  height: 6vh;
  margin-top: 2.5vh;
  display: flex;
}
.mycomment .top .left {
  width: 5vh;
  height: 5vh;
  margin: auto;
  margin-left: 0;
  margin-right: 0;
  border-radius: 50%;
}
.top .right {
  margin-left: 15px;
  border-radius: 8px;
  border: 1px solid rgb(214, 31, 31);
  width: calc(100% - 5vh - 15px);
  height: 6vh;
}
.right textarea {
  outline: none;
  resize: none;
  border: none;
  margin-top: 0.5vh;
  padding-top: 1vh;
  padding-left: 10px;
  padding-right: 10px;
  border-radius: 8px;
  width: calc(100% - 20px);
  height: 4vh;
  font-size: 16px;
  font-weight: 500;
  line-height: 22px;
  color: black;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
.right .text-active {
  padding-top: 0.5vh;
  height: 5vh;
}
.mycomment .button {
  margin-left: 85%;
  min-width: 50px;
  width: 10%;
  height: 3.5vh;
  margin-top: 2vh;
  border-radius: 5px;
  background-color: rgb(214, 31, 31);
  color: white;
  text-align: center;
  font-size: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  justify-content: center;
  cursor: pointer;
  user-select: none;
}
.left .image {
  border-radius: 50%;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  user-select: none;
}
</style>
