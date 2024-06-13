<template>
  <div class="edit-main">
    <div class="edit-wrapper">
      <div class="sidebar">
        <div class="left">
          <el-tooltip content="基本信息" placement="right" hide-after="0">
            <div
              :class="{ item: true, 'item-activate': sideType === 1 }"
              @click="changeSideType(1)"
            >
              <el-icon><Memo /></el-icon>
            </div>
          </el-tooltip>
          <el-tooltip content="文章封面" placement="right" hide-after="0">
            <div
              :class="{ item: true, 'item-activate': sideType === 2 }"
              @click="changeSideType(2)"
            >
              <el-icon><Picture /></el-icon>
            </div>
          </el-tooltip>
          <el-tooltip content="编辑设置" placement="right" hide-after="0">
            <div
              :class="{ item: true, 'item-activate': sideType === 3 }"
              @click="changeSideType(3)"
            >
              <el-icon><Setting /></el-icon>
            </div>
          </el-tooltip>
        </div>
        <div class="right">
          <div class="item" v-if="sideType === 1">
            <div class="title">文章基本信息</div>
            <div class="input">
              <input
                type="text"
                class="input-item"
                placeholder="标题"
                v-model="title"
              />
              <label class="label" id="label-1">标题</label>
            </div>
            <div class="input">
              <input
                type="text"
                class="input-item"
                placeholder="副标题"
                v-model="subtitle"
              />
              <label class="label" id="label-2">副标题</label>
            </div>
            <div class="input">
              <textarea
                class="input-item"
                placeholder="摘要"
                v-model="summary"
              />
              <label class="label" id="label-3">摘要</label>
            </div>
            <div class="tag" v-if="tagnum >= 1">
              <div class="input">
                <input
                  type="text"
                  class="input-item"
                  id="tag-input"
                  placeholder="标签"
                  v-model="tag1.tag"
                />
                <label class="label" id="label-4">标签</label>
              </div>
              <div class="add-icon" @click="addTagNum()">
                <el-icon><CirclePlus /></el-icon>
              </div>
            </div>
            <div class="tag" v-if="tagnum >= 2">
              <div class="input">
                <input
                  type="text"
                  class="input-item"
                  id="tag-input"
                  placeholder="标签"
                  v-model="tag2.tag"
                />
                <label class="label" id="label-5">标签</label>
              </div>
              <div class="add-icon" @click="addTagNum()">
                <el-icon><CirclePlus /></el-icon>
              </div>
            </div>
            <div class="tag" v-if="tag2.tag && tagnum >= 3">
              <div class="input">
                <input
                  type="text"
                  class="input-item"
                  id="tag-input"
                  placeholder="标签"
                  v-model="tag3.tag"
                />
                <label class="label" id="label-6">标签</label>
              </div>
              <div class="add-icon" @click="addTagNum()">
                <el-icon><CirclePlus /></el-icon>
              </div>
            </div>
            <div class="button" @click="submit()">提交</div>
          </div>
          <div class="item" v-if="sideType === 2">
            <div class="title">文章封面</div>
            <div class="photo">
              <div
                class="file-button"
                @click="selectFile()"
                @drop="dropFile($event)"
                @dragover="dragOverFile($event)"
              >
                <div class="icon">
                  <el-icon><Plus /></el-icon>
                </div>
                <div class="note">拖拽图片上传或<span> 点击此处</span></div>
              </div>
              <div class="note-bottom">支持png/jpg格式，大小不超过5MB</div>
              <input
                type="file"
                name="file"
                accept="image/*"
                @change="getFile"
                ref="inputer"
                class="file-input"
              />
              <img :src="imgurl" class="image" v-if="idxPhoto !== 0" />
            </div>
          </div>
          <div class="item" v-if="sideType === 3">
            <div class="title">编辑模式</div>
            <div class="button-list">
              <div class="button" @click="changeEditType(2)">富文本</div>
              <div class="button" @click="changeEditType(1)">Markdown</div>
            </div>
          </div>
        </div>
      </div>
      <div class="edit-panel">
        <div class="md" v-if="editType === 1">
          <mdEditor
            class="md-editor"
            :value="content"
            :locale="zhHans"
            @change="contentChange"
          />
        </div>
        <div class="html" v-if="editType === 2">
          <Editor
            api-key="eu436plht07ucwqgvyq4kxr46gko5hgkzfpdyj0n8tubcjf5"
            class="html-editor"
            :init="editorinit"
            v-model="content"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ElNotification, ElMessage } from "element-plus";
import { ref, getCurrentInstance, onMounted } from "vue";
import "bytemd/dist/index.css";
import { Editor as mdEditor } from "@bytemd/vue-next";
import Editor from "@tinymce/tinymce-vue";
import zhHans from "bytemd/lib/locales/zh_Hans.json";
import { getDate } from "@/utils/getDate";
const { proxy } = getCurrentInstance();
let tag1 = ref("");
let tag2 = ref("");
let tag3 = ref("");
let tagnum = ref(1);
let title = ref();
let subtitle = ref();
let summary = ref();
let tag = ref([]);
let content = ref("");
let sideType = ref(1);
let editType = ref(1);
let file;
let idxPhoto = ref(0);
let imgurl = ref();
let idxArticle;
let url;
let articlePhoto = ref(0);

const selectFile = () => {
  let fileInput = document.getElementsByClassName("file-input")[0];
  fileInput.click();
};

const dropFile = (e) => {
  e.preventDefault();
  file = e.dataTransfer.files[0];
  if (file !== undefined) {
    if (file.size > 1024 * 1024) {
      ElMessage({
        showClose: false,
        message: "图片过大",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: true,
      });
    } else {
      let formData = new FormData();
      formData.append("file", file);
      proxy.$axios
        .post("/image/insert/article", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          if (response.status === 200 && response.data) {
            idxPhoto.value = response.data.data.id;
            imgurl.value = response.data.data.url;
          }
        })
        .catch((error) => {});
    }
  }
};

const dragOverFile = (e) => {
  e.preventDefault();
};

const getFile = () => {
  let inputDOM = proxy.$refs.inputer;
  file = inputDOM.files[0];
  if (file !== undefined) {
    if (file.size > 1024 * 1024) {
      ElMessage({
        showClose: false,
        message: "图片过大",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: true,
      });
    } else {
      let formData = new FormData();
      formData.append("file", file);
      proxy.$axios
        .post("/image/insert/article", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          if (response.status === 200 && response.data) {
            idxPhoto.value = response.data.data.id;
            imgurl.value = response.data.data.url;
          }
        })
        .catch((error) => {});
    }
  }
};

// const getImg = () => {
//   let url = `/image/get/user?idxPhoto=${idxPhoto.value}`;
//   proxy.$axios
//     .get(url, { responseType: "blob" })
//     .then((response) => {
//       if (response.status === 200 && response.data) {
//         blob = new window.Blob([response.data]);
//         imgurl.value = window.URL.createObjectURL(blob);
//       }
//     })
//     .catch((error) => {
//       ElNotification({
//         title: "提交失败",
//         message: error,
//         type: "error",
//       });
//     });
// };

const addTagNum = () => {
  if (tagnum.value <= 2) {
    tagnum.value = tagnum.value + 1;
  } else {
    ElMessage({
      showClose: false,
      message: "标签不能多于三个",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: false,
    });
  }
};

const contentChange = (e) => {
  content.value = e;
};

const changeSideType = (type) => {
  sideType.value = type;
};
const changeEditType = (type) => {
  editType.value = type;
};

const submit = () => {
  let map = new Map();
  map
    .set(tag1.value.id, tag1.value.tag)
    .set(tag2.value.id, tag2.value.tag)
    .set(tag3.value.id, tag3.value.tag);
  let tagmap = Object.fromEntries(map);
  proxy.$axios
    .post("/article/update/mysql", {
      id: idxArticle,
      title: title.value,
      subtitle: subtitle.value,
      summary: summary.value,
      gmtModify: getDate(),
      editType: editType.value,
      content: content.value,
      articlePhoto: idxPhoto.value,
      updateTagMap: tagmap,
    })
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        ElNotification({
          title: "提交成功",
          message: "稍后可以预览",
          type: "success",
        });
        let readRoute = proxy.$router.resolve({
          name: "read",
          query: {
            idxArticle: idxArticle,
          },
        });
        window.open(readRoute.href, "_self");
      } else {

      }
    })
    .catch((error) => {

    });
};

const editorinit = ref({
  toolbar_mode: "sliding",
  toolbar:
    "undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | align lineheight | tinycomments | checklist numlist bullist indent outdent | emoticons charmap | removeformat",
  tinycomments_mode: "embedded",
  tinycomments_author: "Author name",
  language: "zh_CN",
  resize: false,
  branding: false,
  height: "100vh",
  width: "100%",
  mergetags_list: [
    { value: "First.Name", title: "First Name" },
    { value: "Email", title: "Email" },
  ],
});

onMounted(() => {
  idxArticle = proxy.$route.query.idxArticle;

  url = `/article/get/update?idxArticle=${idxArticle}`;
  proxy.$axios
    .get(url)
    .then((response) => {
      if (response.status === 200 && response.data.code === 1) {
        title.value = response.data.data.title;
        subtitle.value = response.data.data.subtitle;
        summary.value = response.data.data.summary;
        articlePhoto.value = response.data.data.articlePhoto;
        imgurl.value = response.data.data.articlePhotoUrl;
        idxPhoto.value = articlePhoto.value;
        content.value = response.data.data.content;
        editType.value = response.data.data.editType;
        tag.value = response.data.data.tagList;
        tagnum.value = tag.value.length;
        tag1.value = tag.value[0];
        tag2.value = tag.value[1];
        tag3.value = tag.value[2];
      }
    })
    .catch((error) => {
      ElNotification({
        title: "失败",
        message: error,
        type: "error",
      });
    });
});
</script>
<style scoped>
.edit-main {
  width: 100%;
  height: 100vh;
}
.edit-main .edit-wrapper {
  width: 100%;
  height: 100vh;
  display: flex;
}
.edit-wrapper .sidebar {
  width: 25%;
  height: 100%;
  display: flex;
  flex-direction: row;
}
.edit-wrapper .sidebar .left {
  min-width: 6vh;
  height: 100%;
  display: flex;
  color: rgba(255, 255, 255, 0.6);
  flex-direction: column;
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
}
.edit-wrapper .sidebar .left .item {
  padding: 1vh;
  cursor: pointer;
  padding-left: 0.6vh;
  border-left: 3px solid transparent;
  height: 6vh;
  font-size: 32px;
  font-weight: 700;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  transition: all 0.1s ease;
}
.edit-wrapper .sidebar .left .item:hover {
  color: white;
  transition: all 0.1s ease;
}
.edit-wrapper .sidebar .left .item-activate {
  border-left: 3px solid #66ccff;
  color: white;
}
.edit-wrapper .sidebar .right {
  width: 100%;
  border-top: 2px solid #eee;
}
.edit-wrapper .sidebar .right .input {
  margin-top: 1.5vh;
  margin-bottom: 1.5vh;
  margin-left: 5%;
  width: 90%;
}
.edit-wrapper .edit-panel {
  width: 75%;
  height: 100vh;
}
.edit-wrapper .edit-panel .md {
  width: 100%;
  height: 100%;
  touch-action: none;
}

:deep(.md-editor .bytemd) {
  height: 100vh;
}
:deep(.html .tox-tinymce) {
  border-radius: 0px;
}

:deep(.header-main) {
  box-shadow: 0 0 0;
}

.input-item {
  outline: none;
  height: 5vh;
  line-height: 34px;
  box-sizing: border-box;
  padding-top: 1vh;
  padding-left: 0.5vh;
  font-size: 16px;
  font-weight: 500;
  border: 1px solid #ccc;
  border-radius: 4px;
  color: black;
  width: 100%;
  text-indent: 5px;
}

.input-item:not(:placeholder-shown),
.input-item:focus {
  border-color: rgb(214, 31, 31);
}

.input-item::placeholder {
  color: transparent;
}

.right .item .title {
  margin-top: 4vh;
  margin-left: 2.5vh;
  margin-bottom: 5vh;
  height: 3vh;
  font-size: 20px;
  font-weight: 500;
}

.label {
  position: absolute;
  transition: transform 0.25s;
  font-size: 16px;
  padding: 0 6px;
  pointer-events: none;
  color: #999;
}
#label-1 {
  left: 8.5vh;
  top: 13vh;
}
#label-2 {
  left: 8.5vh;
  top: 19.5vh;
}
#label-3 {
  left: 8.5vh;
  top: 26vh;
}
#label-4 {
  left: 8.5vh;
  top: 43.5vh;
}
#label-5 {
  left: 8.5vh;
  top: 50vh;
}
#label-6 {
  left: 8.5vh;
  top: 56.5vh;
}
.input-item:not(:placeholder-shown) ~ .label,
.input-item:focus ~ .label {
  transform: scale(0.75) translate(0, -20px);
  background: #fff;
  color: rgb(214, 31, 31);
}

.right .button {
  background-color: rgb(214, 31, 31);
  color: white;
  text-align: center;
  margin: 2vh;
  margin-top: 3vh;
  font-size: 16px;
  cursor: pointer;
  user-select: none;
  border-radius: 6px;
  height: 4vh;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.right textarea {
  outline: none;
  resize: none;
  padding-top: 1.5vh;
  padding-left: 1vh;
  padding-right: 0.5vh;
  width: 100%;
  height: 16vh;
  font-size: 16px;
  font-weight: 500;
  line-height: 22px;
  text-indent: 0px;
  color: black;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
.right .item .button-list {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
.right .item .button-list .button {
  width: 50%;
}

.right .item .photo {
  padding: 2vh;
  text-align: center;
}

.file-input {
  outline: none;
  border: none;
  text-align: center;
  display: none;
}
.image {
  margin-top: 3vh;
  width: 100%;
  height: 15vh;
  border-radius: 8px;
}

.file-button {
  width: 100%;
  height: 20vh;
  border-radius: 8px;
  border: 2px dashed rgba(0, 0, 0, 0.3);
  transition: all 0.2s ease;
}
.file-button:hover {
  border: 2px dashed rgb(214, 31, 31);
}
.note {
  text-align: center;
  margin-top: 2vh;
  width: 100%;
  font-size: 14px;
}
.note span {
  color: rgb(214, 31, 31);
}
.file-button .icon {
  width: 100%;
  height: 5vh;
  margin-top: 6vh;
  font-size: 30px;
  color: rgb(214, 31, 31);
  font-weight: 100;
  text-align: center;
}
.note-bottom {
  text-align: center;
  font-size: 12px;
  margin-top: 1vh;
  color: rgba(0, 0, 0, 0.5);
}
.tag {
  display: flex;
  margin-top: -2vh;
  margin-bottom: 0.5vh;
}
.add-icon {
  width: 40px;
  height: 100%;
  margin: auto;
  margin-left: 5px;
  color: rgb(214, 31, 31);
  font-size: 30px;
  cursor: pointer;
}
.add-icon:hover {
  filter: drop-shadow(1px 1px 2px rgba(214, 31, 31, 0.3));
}
</style>
