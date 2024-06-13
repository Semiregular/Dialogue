<template>
  <div class="article-item-main">
    <div class="photo">
      <img :src="articlePhotoUrl" class="image"/>
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
const { proxy } = getCurrentInstance();


let create = ref();
const props = defineProps([
  "userName",
  "userPhotoUrl",
  "title",
  "subtitle",
  "summary",
  "gmtCreate",
  "articleView",
  "articleLike",
  "articlePhotoUrl",
]);
onMounted(() => {
  create.value = relativeDate(props.gmtCreate);
});
</script>
<style scoped>
.article-item-main {
  width: 45%;
  height: 45%;
  opacity: 0;
  display: flex;
  border-radius: 8px;
  margin-left: 0px;
  margin-bottom: 20px;
  flex-direction: row;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 1;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.article-item-main:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.35);
  transition: all 0.3s ease;
}

.article-item-main .photo {
  width: 30%;
  border-radius: 1vh;
  user-select: none;
}
.image {
  width: 100%;
  height: 100%;
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
  font-size: 18px;
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
  max-width: 20px;
  max-height: 20px;
}
.userimage {
  width: 100%;
  height: 100%;
  min-width: 20px;
  min-height: 20px;
  max-width: 20px;
  max-height: 20px;
  border-radius: 50%;
  user-select: none;
}
.article-item-main .user .name {
  font-size: 14px;
  line-height: 16px;
  margin-left: 8px;
  font-weight: 600;
  color: rgb(215, 29, 29);
}
.article-item-main .summary {
  margin-top: 2%;
  height: 38%;
  font-size: 16px;
  font-family: "仿宋";
  line-height: 25px;
  overflow: hidden;
}
.article-item-main .other {
  margin-top: 6%;
  user-select: none;
  height: 3vh;
  line-height: 3vh;
  width: 100%;
  display: flex;
  flex-direction: row;
  font-size: 12px;
  overflow: hidden;
  color: rgba(0, 0, 0, 0.715);
}
.article-item-main .other .view {
  display: flex;
  min-width: 40px;
}
.article-item-main .other .data {
  margin-left: 5px;
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
