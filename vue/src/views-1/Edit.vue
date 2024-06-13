<template>
  <div class="edit-main">
    <Header></Header>
    <div class="edit-wrapper">
      <div :class="{ sidebar: !isHide, 'sidebar-hide': isHide }">
        <div class="left">
          <el-tooltip
            content="基本信息"
            placement="right"
            :hide-after="0"
            :auto-close="600"
          >
            <div
              :class="{ item: true, 'item-activate': sideType === 1 }"
              @click="changeSideType(1)"
            >
              <el-icon><Memo /></el-icon>
            </div>
          </el-tooltip>
          <el-tooltip
            content="文章封面"
            placement="right"
            :hide-after="0"
            :auto-close="600"
          >
            <div
              :class="{ item: true, 'item-activate': sideType === 2 }"
              @click="changeSideType(2)"
            >
              <el-icon><Picture /></el-icon>
            </div>
          </el-tooltip>
          <el-tooltip
            content="编辑设置"
            placement="right"
            :hide-after="0"
            :auto-close="600"
          >
            <div
              :class="{ item: true, 'item-activate': sideType === 3 }"
              @click="changeSideType(3)"
            >
              <el-icon><Setting /></el-icon>
            </div>
          </el-tooltip>
        </div>
        <div class="right" v-show="!isHide">
          <div class="item" v-if="sideType === 1">
            <div class="title">文章基本信息</div>
            <el-tooltip
              content="文章标题非空，且不超过12个字"
              placement="top-start"
              :hide-after="0"
              :auto-close="1000"
            >
              <div class="input">
                <input
                  type="text"
                  class="input-item"
                  placeholder="标题"
                  v-model="title"
                />
                <label class="label" id="label-1">标题</label>
              </div></el-tooltip
            >
            <el-tooltip
              content="文章副标题非空，且不超过12个字"
              placement="top-start"
              :hide-after="0"
              :auto-close="1000"
            >
              <div class="input">
                <input
                  type="text"
                  class="input-item"
                  placeholder="副标题"
                  v-model="subtitle"
                />
                <label class="label" id="label-2">副标题</label>
              </div></el-tooltip
            >
            <el-tooltip
              content="文章摘要非空，且不超过100个字"
              placement="top-start"
              :hide-after="0"
              :auto-close="1000"
            >
              <div class="input">
                <textarea
                  class="input-item"
                  placeholder="摘要"
                  v-model="summary"
                />
                <label class="label" id="label-3">摘要</label>
              </div></el-tooltip
            >

            <div class="tag" v-if="tagnum >= 1">
              <el-tooltip
                content="文章可选标签，最多三个标签"
                placement="top-start"
                :hide-after="0"
                :auto-close="1000"
              >
                <div class="input">
                  <input
                    type="text"
                    class="input-item"
                    id="tag-input"
                    placeholder="标签"
                    v-model="tag1"
                  />
                  <label class="label" id="label-4">标签</label>
                </div></el-tooltip
              >
              <el-tooltip
                content="点击添加标签"
                placement="right"
                :hide-after="0"
                :auto-close="600"
              >
                <div class="add-icon" @click="addTagNum()">
                  <el-icon><CirclePlus /></el-icon>
                </div>
              </el-tooltip>
            </div>

            <div class="tag" v-if="tagnum >= 2">
              <div class="input">
                <input
                  type="text"
                  class="input-item"
                  id="tag-input"
                  placeholder="标签"
                  v-model="tag2"
                />
                <label class="label" id="label-5">标签</label>
              </div>
              <div class="add-icon" @click="addTagNum()">
                <el-icon><CirclePlus /></el-icon>
              </div>
            </div>
            <div class="tag" v-if="tagnum >= 3">
              <div class="input">
                <input
                  type="text"
                  class="input-item"
                  id="tag-input"
                  placeholder="标签"
                  v-model="tag3"
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
              <el-tooltip
                content="上传文章封面"
                placement="top"
                :hide-after="0"
                :auto-close="600"
              >
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
                </div></el-tooltip
              >
              <div class="note-bottom">支持png/jpg格式，大小不超过5MB</div>
              <input
                type="file"
                name="file"
                accept="image/*"
                @change="getFile"
                ref="inputer"
                class="file-input"
              />
              <div class="image-pre">
                <el-tooltip
                  content="封面预览"
                  placement="top"
                  :hide-after="0"
                  :auto-close="600"
                >
                  <img :src="imgurl" class="image" v-if="idxPhoto !== 0"
                /></el-tooltip>
              </div>
            </div>
          </div>
          <div class="item" v-if="sideType === 3">
            <div class="title">编辑模式</div>
            <div class="button-list">
              <el-tooltip
                content="富文本编辑方式，类似Word文档的编辑体验"
                placement="top-start"
                :hide-after="0"
                :auto-close="1000"
              >
                <div class="button" @click="changeEditType(2)">
                  富文本
                </div></el-tooltip
              >
              <el-tooltip
                content="Markdown编辑方式，简洁高效"
                placement="top-start"
                :hide-after="0"
                :auto-close="1000"
              >
                <div class="button" @click="changeEditType(1)">
                  Markdown
                </div></el-tooltip
              >
            </div>
          </div>
        </div>
      </div>
      <div :class="{ 'edit-panel': !isHide, 'edit-panel-hide': isHide }">
        <div class="md" v-show="editType === 1">
          <mdEditor
            class="md-editor"
            :value="content"
            :locale="zhHans"
            @change="contentChange"
          />
        </div>
        <div class="html" v-show="editType === 2">
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
import Header from "@/components-1/Header.vue";
import { getCookie } from "@/utils/cookies";
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
let isHide = ref(true);
let subtitle = ref();
let summary = ref();
let userName = ref(
  getCookie("userName").userName ? getCookie("userName").userName : "未登录"
);
let idxUser = ref(
  Number(getCookie("idxUser").idxUser)
    ? Number(getCookie("idxUser").idxUser)
    : 0
);
let userPhoto = ref(
  Number(getCookie("idxPhoto").idxPhoto)
    ? Number(getCookie("idxPhoto").idxPhoto)
    : 0
);
let content = ref("");
let sideType = ref(1);
let editType = ref(1);
let file;
let idxPhoto = ref(0);
let imgurl = ref();
let idxArticle;

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

const contentChange = (e) => {
  content.value = e;
};

const changeSideType = (type) => {
  if (sideType.value === type && isHide.value === false) {
    isHide.value = true;
  } else {
    sideType.value = type;
    isHide.value = false;
  }
};
const changeEditType = (type) => {
  editType.value = type;
};

const submit = () => {
  if (idxPhoto.value === 0) {
    ElMessage({
      showClose: false,
      message: "您还未提交封面",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: true,
    });
  } else {
    if (title.value && subtitle.value && summary.value && content.value) {
      proxy.$axios
        .post("/article/insert/mysql", {
          idxUser: idxUser.value,
          userPhoto: userPhoto.value,
          userName: userName.value,
          title: title.value,
          subtitle: subtitle.value,
          summary: summary.value,
          gmtCreate: getDate(),
          tag: [tag1.value, tag2.value, tag3.value],
          editType: editType.value,
          content: content.value,
          articlePhoto: idxPhoto.value,
        })
        .then((response) => {
          if (response.status === 200 && response.data.code === 1) {
            idxArticle = response.data.data;
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
        .catch((error) => {});
    } else {
      ElMessage({
        showClose: false,
        message: "文章信息不完整，请补充",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: true,
      });
    }
  }
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
  height: "92vh",
  width: "100%",
  mergetags_list: [
    { value: "First.Name", title: "First Name" },
    { value: "Email", title: "Email" },
  ],
});
</script>
<style scoped>
.edit-main {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}
.edit-main .edit-wrapper {
  width: 100%;
  height: 92vh;
  display: flex;
}
.edit-wrapper .sidebar {
  width: 25%;
  height: 100%;
  display: flex;
  flex-direction: row;
}
.sidebar-hide {
  width: 50px;
  height: 100%;
  display: flex;
  flex-direction: row;
}
.edit-wrapper .left {
  min-width: 50px;
  height: 100%;
  display: flex;
  color: rgb(214, 31, 31);
  flex-direction: column;
  border-right: 1px solid #eee;
  border-top: 1px solid #eee;
}
.edit-wrapper .left .item {
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
  border-radius: 5px;
}
.edit-wrapper .left .item:hover {
  background-color: #fde2e2;
  transition: all 0.1s ease;
}

:deep(.tool .item:nth-child(3)) {
  display: none;
}
.edit-wrapper .left .item-activate {
  background-color: #fde2e2;
  transition: all 0.1s ease;
}
.edit-wrapper .right {
  width: 100%;
  border-top: 1px solid #eee;
}
.edit-wrapper .right .input {
  margin-top: 1.5vh;
  margin-bottom: 1.5vh;
  margin-left: 5%;
  width: 90%;
}
.edit-wrapper .edit-panel {
  width: 75%;
  height: 100%;
}
.edit-panel-hide {
  width: calc(100% - 50px);
  height: 100%;
}
.edit-wrapper .md {
  width: 100%;
  height: 100%;
  touch-action: none;
}
.tag {
  display: flex;
  margin-top: -2vh;
  margin-bottom: 0.5vh;
}
:deep(.md-editor .bytemd) {
  height: 92vh;
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
  font-size: 16px;
  font-weight: 500;
  padding-top: 1vh;
  padding-left: 0.5vh;
  border: 1px solid #ccc;
  border-radius: 4px;
  color: black;
  width: 100%;
  text-indent: 5px;
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
  top: 21vh;
}
#label-2 {
  left: 8.5vh;
  top: 27.5vh;
}
#label-3 {
  left: 8.5vh;
  top: 34vh;
}
#label-4 {
  left: 8.5vh;
  top: 51.5vh;
}
#label-5 {
  left: 8.5vh;
  top: 58vh;
}
#label-6 {
  left: 8.5vh;
  top: 64.5vh;
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
.image-pre {
  margin-top: 3vh;
  width: 100%;
}
.image {
  max-width: 100%;
  height: auto;
  max-height: 40vh;
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
:deep(.search) {
  opacity: 0;
  z-index: -5;
}
</style>
