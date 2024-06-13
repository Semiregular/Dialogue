<template>
  <div class="model-main">
    <div class="note">语音朗读</div>
    <div class="art">
      <textarea v-model="artText"></textarea>
    </div>
    <div class="lis">
      <!-- <audio
        :src="audioPath"
        ref="audio"
        controls="true"
        autoplay="false"
      ></audio> -->
      <AudioPlayer :audioUrl="audioPath" :isPauseTtsAudio="isPlay" v-if="!isPlay">
      </AudioPlayer>
    </div>
    <div class="req">
      <button @click="start()">朗读</button>
    </div>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance, onMounted } from "vue";
import { ElNotification, ElMessage } from "element-plus";
import { getCookie } from "@/utils/cookies";
import AudioPlayer from "@/components-1/AudioPlayer.vue";
import Header from "@/components-1/Header.vue";
import CryptoJS from "crypto-js";
const { proxy } = getCurrentInstance();

const APPID = "8e741b84";
const API_SECRET = "ZGU3Mzc0YzA4YWIzZWYwZjExMTY2NjFj";
const API_KEY = "ffa1ffdb3ad78108b9421c176f249169";

const props = defineProps(["artraw"]);

let websocket;
let url;
let isPlay = ref(true)
let path;
let audioPath = ref("");
let total = "";
let resAudio = "";
let mime;
let idxUser = ref(
  Number(getCookie("idxUser").idxUser)
    ? Number(getCookie("idxUser").idxUser)
    : 0
);

const init = () => {
  var apiKey = API_KEY;
  var apiSecret = API_SECRET;
  url = "wss://tts-api.xfyun.cn/v2/tts";
  var host = location.host;
  var date = new Date().toGMTString();
  var algorithm = "hmac-sha256";
  var headers = "host date request-line";
  var signatureOrigin = `host: ${host}\ndate: ${date}\nGET /v2/tts HTTP/1.1`;
  var signatureSha = CryptoJS.HmacSHA256(signatureOrigin, apiSecret);
  var signature = CryptoJS.enc.Base64.stringify(signatureSha);
  var authorizationOrigin = `api_key="${apiKey}", algorithm="${algorithm}", headers="${headers}", signature="${signature}"`;
  var authorization = btoa(authorizationOrigin);
  url = `${url}?authorization=${authorization}&date=${date}&host=${host}`;
};

const encodeText = (text, type) => {
  if (type === "unicode") {
    let buf = new ArrayBuffer(text.length * 4);
    let bufView = new Uint16Array(buf);
    for (let i = 0, strlen = text.length; i < strlen; i++) {
      bufView[i] = text.charCodeAt(i);
    }
    let binary = "";
    let bytes = new Uint8Array(buf);
    let len = bytes.byteLength;
    for (let i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
  }
};
const send = () => {
  total = "";
  isPlay.value = true;
  audioPath.value = '';
  var params = {
    common: {
      app_id: APPID,
    },
    business: {
      aue: "lame",
      auf: "audio/L16;rate=16000",
      sfl: 1,
      vcn: "xiaoyan",
      speed: +50,
      volume: +50,
      pitch: +50,
      bgs: 0,
      tte: "unicode",
    },
    data: {
      status: 2,
      text: encodeText(artText.value, "unicode"),
    },
  };
  let req = JSON.stringify(params);
  websocket.send(req);
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
      if (res.code !== 0) {
        return;
      }
      resAudio = res.data.audio;
      base64ToBlob(resAudio, "mp3");
      if (res.code === 0 && res.data.status === 2) {
        websocket.close();
        let ab = new ArrayBuffer(total.length);
        let ia = new Uint8Array(ab);
        for (let i = 0; i < total.length; i++) {
          ia[i] = total.charCodeAt(i);
        }
        let blob = new Blob([ia], {
          type: mime,
        });
        path = window.URL.createObjectURL(blob);
        changePath();
      }
    };
  } else {

  }
};

const changePath = () => {
  audioPath.value = path;
  isPlay.value = false;

  //proxy.$refs.audio.play().catch((err) => {});
};

function base64ToBlob(base64, fileType) {
  let typeHeader = "data:application/" + fileType + ";base64,";
  let audioSrc = typeHeader + base64;
  let arr = audioSrc.split(",");
  let array = arr[0].match(/:(.*?);/);
  mime = (array && array.length > 1 ? array[1] : fileType) || fileType;
  let bytes = window.atob(arr[1]);
  total = total + bytes;
}

let artText = ref(props.artraw);
artText.value = artText.value.replace(/<[^>]+>/g, '');
artText.value = artText.value.slice(0,500);


// let artText = ref(`
//             春江花月夜
//     春江潮水连海平，海上明月共潮生。
//     滟滟随波千万里，何处春江无月明。
//     江流宛转绕芳甸，月照花林皆似霰。
//     空里流霜不觉飞，汀上白沙看不见。
//     江天一色无纤尘，皎皎空中孤月轮。
//     江畔何人初见月？江月何年初照人？
//     人生代代无穷已，江月年年望相似。
//     不知江月待何人，但见长江送流水。
//     白云一片去悠悠，青枫浦上不胜愁。
//     谁家今夜扁舟子？何处相思明月楼？
//     可怜楼上月徘徊，应照离人妆镜台。
//     玉户帘中卷不去，捣衣砧上拂还来。
//     此时相望不相闻，愿逐月华流照君。
//     鸿雁长飞光不度，鱼龙潜跃水成文。
//     昨夜闲潭梦落花，可怜春半不还家。
//     江水流春去欲尽，江潭落月复西斜。
//     斜月沉沉藏海雾，碣石潇湘无限路。
//     不知乘月几人归，落月摇情满江树。`);

</script>
<style scoped>
.model-main {
  width: 100%;
  height: 100%;
}
.art {
  width: 100%;
  height: calc(100% - 123px);
}
.art textarea {
  resize: none;
  outline: none;
  border: none;
  font-size: 13px;
  padding-left: 10px;
  padding-right: 10px;
  padding-top: 5px;
  padding-bottom: 5px;
  line-height: 25px;
  width: calc(100% - 20px);
  height: calc(100% - 10px);
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
.lis {
  height: 40px;
  width: calc(100% - 20px);
  margin-left: 10px;
  margin-right: 10px;
  margin-top: 5px;
  margin-bottom: 5px;
}
.req {
  width: 100%;
  height: 36px;
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
  margin-top: 5px;
  border-radius: 5px;
  background-color: rgb(214, 31, 31);
  color: white;
  font-size: 14px;
  text-align: center;
  flex-direction: column;
  display: flex;
  justify-content: center;
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
