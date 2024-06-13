<template>
  <div class="article-item-main">
    <div class="photo">
      <img :src="articlePhotoUrl" class="image" @error="setDefaultImage" />
    </div>
    <div class="article">
      <div class="title">
        <div class="maintitle">{{ title }}</div>
        <div class="subtitle">{{ subtitle }}</div>
      </div>
      <div class="user">
        <div class="user-photo">
          <img :src="userPhotoUrl" class="userimage" />
        </div>
        <div class="name">{{ userName }}</div>
      </div>
      <div class="summary">{{ summary }}</div>
      <div class="other">
        <div class="view">
          <div class="icon">
            <el-icon><View /></el-icon>
          </div>
          <div class="data">{{ articleView }}</div>
        </div>
        <div class="like">
          <div class="icon">
            <el-icon><ArrowUp /></el-icon>
          </div>
          <div class="data">{{ articleLike }}</div>
        </div>
        <div class="time">
          {{ create }}
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref, getCurrentInstance } from "vue";
import { relativeDate } from "@/utils/getDate";

let create = ref();
const props = defineProps([
  "id",
  "idxUser",
  "userName",
  "userPhotoUrl",
  "title",
  "subtitle",
  "summary",
  "gmtCreate",
  "articleView",
  "articleLike",
  "articleCollect",
  "articleShare",
  "articlePhotoUrl",
]);

const setDefaultImage = (e) => {
  e.target.src = "../assets/pic-9.jpg";
};
onMounted(() => {
  create.value = relativeDate(props.gmtCreate);
});
</script>
<style scoped>
.article-item-main {
  width: 45%;
  height: 25vh;
  opacity: 0;
  display: flex;
  border-radius: 8px;
  margin-left: 30px;
  margin-bottom: 30px;
  flex-direction: row;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
}
.article-item-main:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}
.article-item-main .photo {
  width: 30%;
  overflow: hidden;
  height: 100%;
  border-radius: 1vh;
  user-select: none;
}
.image {
  width: 100%;
  min-height: 100%;
  border-radius: 1vh;
}
.article-item-main .article {
  width: calc(65% - 10px);
  margin-left: 3%;
  display: flex;
  flex-direction: column;
}
.article-item-main .title {
  height: 15%;
  display: flex;
  overflow: hidden;
  margin-top: 1vh;
  flex-direction: row;
  font-size: 20px;
}
.article-item-main .title .maintitle {
  font-weight: 600;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.article-item-main .title .subtitle {
  margin-left: 3%;
  margin-top: 0.2vh;
  font-size: 16px;
  font-weight: 400;
  color: rgba(0, 0, 0, 0.75);
  display: flex;
  flex-direction: column;
  justify-content: center;
  overflow: hidden;
}
.article-item-main .user {
  display: flex;
  user-select: none;
  flex-direction: row;
  margin-top: 1vh;
  height: 18px;
}
.article-item-main .user .userphoto {
  max-width: 18px;
  max-height: 18px;
}
.userimage {
  width: 100%;
  height: 100%;
  max-width: 18px;
  max-height: 18px;
  border-radius: 50%;
  user-select: none;
}
.article-item-main .user .name {
  font-size: 14px;
  margin-left: 8px;
  font-weight: 600;
  color: rgb(215, 29, 29);
}
.article-item-main .summary {
  margin-top: 2%;
  height: 35%;
  font-size: 16px;
  line-height: 25px;
  overflow: hidden;
  font-family: "仿宋";
}
.article-item-main .other {
  margin-top: 5%;
  user-select: none;
  height: 3vh;
  line-height: 3vh;
  width: 100%;
  display: flex;
  flex-direction: row;
  font-size: 12px;
  font-weight: 400;
  overflow: hidden;
  color: rgba(0, 0, 0, 0.715);
}
.article-item-main .other .view {
  display: flex;
  min-width: 40px;
}
.article-item-main .other .data {
  margin-left: 0.5vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.article-item-main .other .like {
  display: flex;

  margin-left: 5%;
  min-width: 40px;
}
.article-item-main .other .time {
  margin-left: calc(95% - 150px);
  width: 60px;
  text-align: right;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.article-item-main .other .icon {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
</style>
