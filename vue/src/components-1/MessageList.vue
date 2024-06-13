<template>
  <div class="message-bar">
    <div class="receive-bar" v-if="idxTo === idxUser">
      <div class="user">
        <div class="photo">
          <img :src="imgurl" class="image" @error="setDefaultImage"  />
        </div>
      </div>
      <div class="text">
        <div class="name">{{ nameFrom }}</div>
        <div class="content" v-if="type === 1">
          <p>{{ content }}</p>
        </div>
        <div class="content-image" v-if="type === 2">
          <img :src="content" class="msg-img" />
        </div>
      </div>
    </div>
    <div class="send-bar" v-if="idxFrom === idxUser">
      <div class="user">
        <div class="photo">
          <img :src="imgurl" class="image" @error="setDefaultImage" />
        </div>
      </div>
      <div class="text">
        <div class="content" v-if="type === 1">
          <p>{{ content }}</p>
        </div>
        <div class="content-image" v-if="type === 2">
          <img :src="content" class="msg-img" />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance, onMounted } from "vue";
import { getCookie } from "@/utils/cookies";
const { proxy } = getCurrentInstance();
const props = defineProps([
  "idxFrom",
  "idxTo",
  "nameFrom",
  "nameTo",
  "content",
  "gmtCreate",
  "photoFromUrl",
  "type",
]);
let idxUser = ref(Number(getCookie("idxUser").idxUser));
let blob;

let url;
let imgurl = ref();
let idxPhoto = ref(0);

const setDefaultImage = (e) => {
  e.target.src = "../assets/pic-8.jpg";
};
onMounted(() => {
  imgurl.value = props.photoFromUrl;
});
</script>
<style scoped>
.message-bar {
  width: 100%;
  min-height: 5vh;
  display: flex;
  flex-direction: row;
  margin-bottom: 3vh;
}
.image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.message-bar .receive-bar {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: row;
}
.message-bar .receive-bar .user {
  width: 8%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
}
.message-bar .receive-bar .user .photo {
  width: 5vh;
  height: 5vh;
  border-radius: 50%;
  user-select: none;
}
.message-bar .receive-bar .text {
  max-width: 75%;
  margin-left: 3px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  text-align: left;
}
.message-bar .receive-bar .text .name {
  font-size: 12px;
  font-size: 500;
  user-select: none;
}
.message-bar .receive-bar .text .content {
  font-size: 14px;
  font-size: 400;
  margin-top: 5px;
  padding-left: 8px;
  padding-right: 8px;
  padding-top: 8px;
  padding-bottom: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  border-radius: 5px;
  background-color: rgb(141, 210, 244);
}
.message-bar .receive-bar .text .content p {
  white-space: pre-wrap;
  word-wrap: break-word;
}
.message-bar .send-bar {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: row-reverse;
}
.message-bar .send-bar .user {
  width: 10%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
}
.message-bar .send-bar .user .photo {
  width: 5vh;
  height: 5vh;
  border-radius: 50%;
  user-select: none;
}
.message-bar .send-bar .text {
  max-width: 75%;
  margin-right: -2px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  text-align: left;
}
.message-bar .send-bar .text .content {
  font-size: 14px;
  font-size: 400;
  padding: 8px;
  color: white;
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
  border-radius: 5px;
}
.message-bar .send-bar .text .content p {
  white-space: pre-wrap;
  word-wrap: break-word;
}
.send-bar .content-image {
  margin-top: 5px;
  text-align: right;
  height: 100px;
  margin-bottom: 1.5vh;
}
.receive-bar .content-image{
  margin-top: 5px;
  text-align: left;
  height: 100px;
  margin-bottom: 1.5vh;
}
.msg-img {
  width: auto;
  max-height: 100%;
  max-width: 100%;
}
</style>
