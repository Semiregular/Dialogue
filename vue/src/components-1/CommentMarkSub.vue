<template>
  <div class="subcomment-container">
    <div class="subcomment-main">
      <div class="left">
        <div class="photo" @click="openOtherInfo()">
          <img :src="userPhotoUrl" class="image"  />
        </div>
      </div>
      <div class="right">
        <div class="name" @click="openOtherInfo()">{{ authorName }}</div>
        <div class="content">{{ content }}</div>
        <div class="info">
          <div class="item">IP属地：{{ props.ip }}</div>
          <div class="item">{{ create }}</div>
          <div
            :class="{ item: true, 'item-active': isLike === 1 }"
            @click="changeLike()"
          >
            <div class="icon">
              <el-icon><ArrowUp /></el-icon>
            </div>
            <div>{{ likenum }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance, onMounted } from "vue";
import { getCookie } from "@/utils/cookies";
import { relativeDate, getDate } from "@/utils/getDate";
const props = defineProps([
  "id",
  "idxAuthor",
  "authorName",
  "content",
  "gmtCreate",
  "commentLike",
  "userPhotoUrl",
  "idxComment",
  "ip",
  "idxArticle",
  "isLike"
]);

const { proxy } = getCurrentInstance();
let create = ref();
let id;
let isLike = ref(0);
let likenum = ref(0);
let idxUser = ref(
  Number(getCookie("idxUser").idxUser)
    ? Number(getCookie("idxUser").idxUser)
    : 0
);

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
    .post("/user/update/comment/history", {
      idxComment: id,
      idxUser: idxUser.value,
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

onMounted(() => {
  id = Number(props.id);
  likenum.value = Number(props.commentLike);
  create.value = relativeDate(props.gmtCreate);
  if(Number(props.isLike) !== 2){
    isLike.value = Number(props.isLike);
  };
});
</script>
<style scoped>
.image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.subcomment-container {
  width: 90%;
  margin-left: calc(4vh + 5px);
  margin-top: 20px;
  min-height: 4vh;
}
.subcomment-main {
  width: 100%;
  min-height: 4vh;
  display: flex;
  flex-direction: row;
}
.subcomment-main .left {
  width: 5vh;
  height: 100%;
}
.subcomment-main .left .photo {
  min-width: 4vh;
  min-height: 4vh;
  border-radius: 50%;
  cursor: pointer;
  user-select: none;
}
.subcomment-main .right {
  margin-left: 15px;
  width: 90%;
  display: flex;
  flex-direction: column;
}
.subcomment-main .right .name {
  font-size: 13px;
  font-weight: 600;
  color: rgb(214, 31, 31);
  user-select: none;
  width: fit-content;
  cursor: pointer;
}
.subcomment-main .right .content {
  margin-top: 6px;
  font-size: 13px;
  font-size: 400;
}
.subcomment-main .right .info {
  margin-top: 6px;
  user-select: none;
  display: flex;
  flex-direction: row;
  font-size: 11px;
  color: rgba(0, 0, 0, 0.5);
}
.subcomment-main .right .info .item {
  display: flex;
  margin-right: 20px;
}
.subcomment-main .right .info .item .icon {
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

.mysubcomment {
  width: 100%;
  height: 9vh;
  overflow: hidden;
  border-radius: 10px;
  background-color: white;
  transition: all 0.5s ease;
}
.mysubcomment-active {
  height: 14vh;
  transition: all 0.5s ease;
}
.mysubcomment .top {
  width: 100%;
  height: 6vh;
  margin-top: 2vh;
  display: flex;
}
.mysubcomment .top .left {
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
.mysubcomment .button {
  margin-left: 90%;
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
  min-width: 100%;
  min-height: 100%;
  user-select: none;
}
.info .item:nth-child(4) {
  cursor: pointer;
}
.item-active {
  color: rgb(214, 31, 31);
}
</style>
