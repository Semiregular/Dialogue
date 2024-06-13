<template>
  <div class="comment-container">
    <div class="comment-main">
      <div class="left">
        <div class="photo" @click="openOtherInfo()">
          <img :src="userPhotoUrl" class="image" />
        </div>
      </div>
      <div class="right">
        <div class="name" @click="openOtherInfo()">{{ authorName }}</div>
        <div class="content">{{ content }}</div>
        <div class="info">
          <div class="item">IP属地：{{ props.ip }}</div>
          <div class="item">{{ create }}</div>
          <div :class="{ item: true, 'item-active': isLike === 1 }" @click="changeLike()">
            <div class="icon">
              <el-icon><ArrowUp /></el-icon>
            </div>
            <div>{{ likenum }}</div>
          </div>
          <div class="item" @click="changeComment()">
            <div class="icon">
              <el-icon><ChatRound /></el-icon>
            </div>
            <div>{{ subcomment.length }}</div>
          </div>
        </div>
      </div>
    </div>
    <div
      :class="{ mymaincomment: true, 'mymaincomment-active': isClick }"
      v-if="isComment"
    >
      <div class="top">
        <div class="left">
          <img :src="mainimgurl" class="image" v-if="idxPhoto !== 0" />
          <img src="../assets/pic-8.jpg" class="image" v-if="idxPhoto === 0" />
        </div>
        <div class="right">
          <textarea
            placeholder="期待你的精彩评论！(*^▽^*)"
            v-model="mymainComment"
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
    <div class="subcomment-list">
      <CommentSub
        v-for="item in subcomment"
        class="subcomment-list-item"
        :id="item.id"
        :idxComment="item.idxComment"
        :idxAuthor="item.idxUser"
        :idxArticle="item.idxArticle"
        :authorName="item.userName"
        :content="item.content"
        :commentLike="item.commentLike"
        :userPhotoUrl="item.userPhotoUrl"
        :isLike="item.isLike"
        :ip="item.ip"
        :gmtCreate="item.gmtCreate"
      ></CommentSub>
    </div>
    <el-divider />
  </div>
</template>
<script setup>
import { ref, getCurrentInstance, onMounted } from "vue";
import { getCookie } from "@/utils/cookies";
import { storeToRefs } from "pinia";
import { useUserStore } from "@/stores/user";
import CommentSub from "./CommentSub.vue";
import { getDate } from "@/utils/getDate";
import { ElNotification, ElMessage } from "element-plus";
import { relativeDate } from "@/utils/getDate";
const props = defineProps([
  "id",
  "idxAuthor",
  "authorName",
  "content",
  "gmtCreate",
  "commentLike",
  "userPhotoUrl",
  "ip",
  "idxArticle",
  "sublist",
  "isLike"
]);
const userStore = useUserStore();
const { ip } = storeToRefs(userStore);
const { proxy } = getCurrentInstance();
let subcomment = ref([]);
let create = ref();
let isComment = ref(false);
let id;
let url;
let blob;
let idxPhoto = ref(0);
let imgurl = ref();
let isEnter = ref(false);
let mymainComment = ref();
let isInput = ref(false);
let isClick = ref(false);
let isLike = ref(0);
let likenum = ref(0);
let userPhoto = ref(
  Number(getCookie("idxPhoto").idxPhoto)
    ? Number(getCookie("idxPhoto").idxPhoto)
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
let idxHistory;
const changeLike = () => {
  if (isLike.value === 0) {
    isLike.value = 1;
    likenum.value = likenum.value + 1;
    updateHistory();
  } else {
    isLike.value = 0;
    likenum.value = likenum.value - 1;
    updateHistory();
  }
};

const openOtherInfo = () => {
    let otherRoute = proxy.$router.resolve({
      name: "other",
      query: {
        id: Number(props.idxAuthor),
      },
    });
    window.open(otherRoute.href, "_blank");
};

const updateHistory = () => {
  proxy.$axios
    .post("/user/update/history/comment", {
      id: idxHistory,
      isLike: isLike.value,
      gmtModify: getDate(),
    })
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
      }
    })
    .catch((error) => {

    });
};

const submit = () => {
  if (mymainComment.value) {
    proxy.$axios
      .post("/comment/insert", {
        idxArticle: Number(props.idxArticle),
        idxUser: idxUser.value,
        idxComment: Number(props.id),
        ip: ip.value[0],
        userName: userName.value,
        userPhoto: userPhoto.value,
        content: mymainComment.value,
        gmtCreate: getDate(),
        gmtModify: getDate(),
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          mymainComment.value = "";
          isComment.value = false;
          ElMessage({
            showClose: false,
            message: "提交成功",
            type: "success",
            duration: 1000,
            center: true,
            grouping: false,
          });
          location.reload()
        }
      })
      .catch((error) => {
 
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
const changeComment = () => {
  isComment.value = !isComment.value;
};
const changeInput = (e) => {
  if (mymainComment.value) {
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
const changeEnter = (state) => {
  isEnter.value = state;
};

onMounted(() => {
  id = Number(props.id);
  likenum.value = Number(props.commentLike);
  create.value = relativeDate(props.gmtCreate);
  subcomment.value = props.sublist
  if(Number(props.isLike) !== 2){
    isLike.value = Number(props.isLike);
  };
});
</script>
<style scoped>
.comment-container {
  width: 90%;
  min-height: 5vh;
}
.comment-main {
  width: 100%;
  min-height: 5vh;
  display: flex;
  flex-direction: row;
}
.comment-main .left {
  width: 5vh;
  height: 100%;
}
.comment-main .left .photo {
  width: 5vh;
  height: 5vh;
  border-radius: 50%;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  user-select: none;
}
.image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.comment-main .right {
  margin-left: 15px;
  width: 90%;
  display: flex;
  flex-direction: column;
}
.comment-main .right .name {
  font-size: 16px;
  font-size: 500;
  user-select: none;
  cursor: pointer;
  width: fit-content;
}
.comment-main .right .content {
  margin-top: 1vh;
  font-size: 15px;
  font-size: 400;
}
.comment-main .right .info {
  margin-top: 1vh;
  user-select: none;
  display: flex;
  flex-direction: row;
  font-size: 12px;
  color: rgba(0, 0, 0, 0.5);
}
.comment-main .right .info .item {
  display: flex;
  margin-right: 20px;
}
.comment-main .right .info .item .icon {
  margin-right: 3px;
}
.info .item div {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.info .item:nth-child(2) {
  margin: auto;
  margin-left: 0;
}
.info .item:nth-child(3) {
  cursor: pointer;
}
.info .item:nth-child(4) {
  cursor: pointer;
}

.mymaincomment {
  width: 90%;
  margin-left: calc(5vh + 15px);
  height: 9vh;
  overflow: hidden;
  border-radius: 10px;
  background-color: white;
  transition: all 0.5s ease;
}
.mymaincomment-active {
  height: 14vh;
  transition: all 0.5s ease;
}
.mymaincomment .top {
  width: 100%;
  height: 6vh;
  margin-top: 2vh;
  display: flex;
}
.mymaincomment .top .left {
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
.mymaincomment .button {
  margin-left: 80%;
  width: 10%;
  min-width: 50px;
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
.item-active{
  color:rgb(214, 31, 31);
}
</style>
