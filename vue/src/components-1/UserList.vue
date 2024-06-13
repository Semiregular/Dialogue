<template>
  <div class="user-list-main">
    <div class="left">
      <div class="photo" v-if="idxTo === idxUser">
        <img :src="imgurl" class="image"  />
      </div>
      <div class="photo" v-if="idxFrom === idxUser">
        <img :src="imgurl" class="image"  />
      </div>
    </div>
    <div class="middle">
      <div class="name" v-if="idxFrom === idxUser">{{ nameTo }}</div>
      <div class="name" v-if="idxTo === idxUser">{{ nameFrom }}</div>
      <div class="content" v-if="type === 1">{{ content }}</div>
      <div class="content" v-if="type === 2">[图片]</div>
    </div>
    <div class="right">
      <div class="time">{{ gmtModify }}</div>
      <div class="note-warn" v-if="isCheck === 0">未读</div>
      <div class="note" v-if="isCheck === 1"></div>
    </div>
  </div>
</template>
<script setup>
import { getCookie } from "@/utils/cookies";
import { ref, onMounted, getCurrentInstance } from "vue";
import { ElNotification } from "element-plus";
const { proxy } = getCurrentInstance();
let idxUser = ref(Number(getCookie("idxUser").idxUser));
let name = ref(getCookie("userName").userName);
const props = defineProps([
  "id",
  "nameFrom",
  "nameTo",
  "photoFromUrl",
  "photoToUrl",
  "content",
  "idxFrom",
  "idxTo",
  "gmtModify",
  "isCheck",
  "type",
]);
let blob;
let url;
let imgurl = ref();
let idxPhoto = ref(0);

onMounted(() => {
  if (Number(props.idxTo) === idxUser.value) {
    imgurl.value = props.photoFromUrl;
  } else {
    imgurl.value = props.photoToUrl;
  }
});
</script>
<style scoped>
.user-list-main {
  width: 100%;
  height: 10vh;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  user-select: none;
  transition: all 0.3s ease;
}
.image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.user-list-main:hover {
  background-color: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}
.user-list-main .left {
  width: 30%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.user-list-main .photo {
  width: 5vh;
  height: 5vh;
  border-radius: 50%;
  cursor: pointer;
}
.user-list-main .middle {
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.user-list-main .middle .name {
  height: 3vh;
  font-size: 14px;
  font-weight: 500;
}
.user-list-main .middle .content {
  color: rgba(0, 0, 0, 0.5);
  font-size: 14px;
}
.user-list-main .right {
  width: 25%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}
.user-list-main .right .time {
  height: 2vh;
  font-size: 12px;
  color: rgba(0, 0, 0, 0.5);
  overflow: hidden;
}
.user-list-main .right .note {
  height: 3vh;
  margin-top: -3vh;
  font-size: 14px;
  font-weight: 500;
  overflow: hidden;
  color: rgba(216, 5, 5);
}
.note-warn {
  border-radius: 10px;
  background-color: #fde2e2;
  color: rgb(241, 31, 31);
  font-size: 12px;
  font-weight: 500;
  width: 40px;
  text-align: center;
  height: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
</style>
