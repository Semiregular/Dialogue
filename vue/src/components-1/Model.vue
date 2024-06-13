<template>
  <div class="model-main">
    <div class="note">小助手</div>
    <div class="model-chat-list">
      <el-scrollbar height="54vh">
        <ModelList
          v-for="item in totalRes"
          class="model-list-item"
          :role="item.role"
          :content="item.content"
        ></ModelList>
        <div>
          <a id="end" name="1" href="#1"></a>
        </div>
      </el-scrollbar>
    </div>
    <div class="req">
      <textarea v-model="question"></textarea>
      <button @click="start()">发送</button>
    </div>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance, onMounted } from "vue";
import { ElNotification, ElMessage } from "element-plus";
import { getCookie } from "@/utils/cookies";
import Header from "@/components-1/Header.vue";
import ModelList from "@/components-1/ModelList.vue";
import CryptoJS from "crypto-js";
const { proxy } = getCurrentInstance();

const APPID = "8e741b84";
const API_SECRET = "ZGU3Mzc0YzA4YWIzZWYwZjExMTY2NjFj";
const API_KEY = "ffa1ffdb3ad78108b9421c176f249169";
let question = ref("你好");
let websocket;
let url;
let totalRes = ref([]);
let singleRes;
let idxUser = ref(
  Number(getCookie("idxUser").idxUser)
    ? Number(getCookie("idxUser").idxUser)
    : 0
);
totalRes.value.push({ role: "assistant", content: "你好呀，欢迎开始提问！" });

const init = () => {
  var apiKey = API_KEY;
  var apiSecret = API_SECRET;
  url = "wss://spark-api.xf-yun.com/v3.5/chat";
  var host = location.host;
  var date = new Date().toGMTString();
  var algorithm = "hmac-sha256";
  var headers = "host date request-line";
  var signatureOrigin = `host: ${host}\ndate: ${date}\nGET /v3.5/chat HTTP/1.1`;
  var signatureSha = CryptoJS.HmacSHA256(signatureOrigin, apiSecret);
  var signature = CryptoJS.enc.Base64.stringify(signatureSha);
  var authorizationOrigin = `api_key="${apiKey}", algorithm="${algorithm}", headers="${headers}", signature="${signature}"`;
  var authorization = btoa(authorizationOrigin);
  url = `${url}?authorization=${authorization}&date=${date}&host=${host}`;
};

const send = () => {
  totalRes.value.push({ role: "user", content: question.value });
  let end = document.getElementById("end");
  end.click();
  question.value = "";
  var params = {
    header: {
      app_id: APPID,
      uid: String(idxUser.value),
    },
    parameter: {
      chat: {
        domain: "generalv3.5",
        temperature: 0.5,
        max_tokens: 512,
      },
    },
    payload: {
      message: {
        text: totalRes.value,
      },
    },
  };
  params = JSON.stringify(params);
  websocket.send(params);
};

const start = () => {
  init();
  if ("WebSocket" in window) {
    websocket = new WebSocket(url);
    websocket.onerror = function () {

    };

    websocket.onopen = function () {
      send();
    };

    websocket.onmessage = function (event) {
      let res = JSON.parse(event.data);
      if (res.payload.choices.status === 0) {
        singleRes = res.payload.choices.text[0].content;
        totalRes.value.push({ role: "assistant", content: singleRes });
      } else {
        singleRes = singleRes + res.payload.choices.text[0].content;
        totalRes.value.pop();
        totalRes.value.push({ role: "assistant", content: singleRes });
      }
      let end = document.getElementById("end");
      end.click();
    };
  } else {

  }
};
</script>
<style scoped>
.model-main {
  width: 100%;
  height: 100%;
}
.model-chat-list {
  width: 100%;
  height: 75%;
  overflow: hidden;
}
.req {
  width: 100%;
  height: calc(25% - 42px);
  border-top: 2px solid #eee;
}
.req textarea {
  width: calc(100% - 20px);
  height: calc(100% - 46px);
  padding-left: 10px;
  padding-right: 10px;
  padding-top: 5px;
  padding-bottom: 5px;
  font-size: 14px;
  outline: none;
  resize: none;
  border-radius: 8px;
  background-color: white;
  border: none;
}
.req button {
  margin-left: calc(100% - 70px);
  width: 60px;
  height: 26px;
  border: none;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  align-items: center;
  margin-bottom: 5px;
  border-radius: 5px;
  background-color: rgb(214, 31, 31);
  color: white;
  font-size: 14px;
  text-align: center;
  flex-direction: column;
  display: flex;
  justify-content: center;
}
#end {
  user-select: none;
  text-decoration: none;
  color: transparent;
}
.note {
  width: 100%;
  height: 40px;
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-bottom: 2px solid #eee;
  user-select: none;
  color: rgb(214, 31, 31);
}
</style>
