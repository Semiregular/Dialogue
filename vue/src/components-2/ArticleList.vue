<template>
  <div class="article-main">
    <div class="photo">
      <img :src="articlePhotoUrl" class="image"/>
    </div>
    <div class="article">
      <div class="title">
        <div class="maintitle">{{ title }}</div>
        <div class="subtitle">{{ subtitle }}</div>
      </div>
      <div class="user">
        <div class="profile">
          <el-icon><User /></el-icon>
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
          {{ gmtCreate }}
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
const props = defineProps([
  "userName",
  "title",
  "subtitle",
  "summary",
  "gmtCreate",
  "articleView",
  "articleLike",
  "articlePhotoUrl"
]);
</script>
<style scoped>
.article-main {
  width: 100%;
  height: 180px;
  opacity: 0;
  display: flex;
  border-radius: 1vh;
  margin-left: -100px;
  margin-bottom: 5vh;
  flex-direction: row;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}
.article-main .photo {
  width: 30%;
  border-radius: 1vh;
}
.image{
  width:100%;
  height:100%;
}
.article-main .article {
  width: 65%;
  margin-left: 3%;
  display: flex;
  flex-direction: column;
}
.article-main .title {
  height: 3vh;
  display: flex;
  margin-top: 1vh;
  flex-direction: row;
  overflow: hidden;
}
.article-main .title .maintitle {
  font-weight: 600;
  font-size: 18px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.article-main .title .subtitle {
  margin-left: 3%;
  font-size: 14px;
  font-weight: 400;
  color: rgba(0, 0, 0, 0.75);
  display: flex;
  flex-direction: column;
  justify-content: center;
  overflow: hidden;
}
.article-main .user {
  display: flex;
  flex-direction: row;
  margin-top: 1vh;
  height: 2vh;
}
.article-main .user .profile {
  color: rgb(214, 31, 31);
  font-size: 2.2vh;
  font-weight: 600;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.article-main .user .name {
  font-size: 14px;
  margin-left: 5px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.article-main .summary {
  margin-top: 1vh;
  height: 8vh;
  font-size: 14px;
  overflow: hidden;
}
.article-main .other {
  margin-top: 1vh;
  width: 100%;
  height: 2vh;
  display: flex;
  flex-direction: row;
  overflow: hidden;
  font-size: 14px;
  font-weight: 400;
  color: rgba(0, 0, 0, 0.715);
}
.article-main .other .view {
  display: flex;
}
.article-main .other .data {
  margin-left: 0.5vh;
}
.article-main .other .like {
  display: flex;
  margin-left: 5%;
}
.article-main .other .time {
  margin-left: 5%;
}
</style>





  




