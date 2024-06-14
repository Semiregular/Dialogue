package cn.org.dialogue.service;

import cn.org.dialogue.dto.ArticleDTO;
import cn.org.dialogue.elasticsearch.EsFieldConstant;
import cn.org.dialogue.elasticsearch.EsIndexConstant;
import cn.org.dialogue.mapper.ArticleDetailMapper;
import cn.org.dialogue.mapper.ArticlePhotoMapper;
import cn.org.dialogue.mapper.ArticleTagMapper;
import cn.org.dialogue.po.*;
import cn.org.dialogue.mapper.ArticleMapper;
import cn.org.dialogue.res.ResStatusEnum;
import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.vo.ArticleDetailVo;
import cn.org.dialogue.vo.ArticleVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;



import java.io.IOException;
import java.util.*;

@Service
@Component
public class ArticleService {
    @Autowired
    public ArticleMapper articleMapper;
    @Autowired
    public ArticleDetailMapper articleDetailMapper;
    @Autowired
    public ArticleTagMapper articleTagMapper;
    @Autowired
    public ArticleCommentService articleCommentService;
    @Autowired
    public ArticlePhotoMapper articlePhotoMapper;
    @Autowired
    public ImageService imageService;
    /**
     * elasticsearch客户端窗口
     */
    @Autowired
    public RestHighLevelClient restHighLevelClient;

    /**
     * @param idxArticle 文章唯一 Id
     * @return 获取阅读界面文章信息
     */
    public ResVo getArticleForRead(int idxArticle){
        ArticlePo articlePo = articleMapper.selectById(idxArticle);
        String userPhotoUrl = imageService.getUserImageUrl(articlePo.getUserPhoto());
        String articlePhotoUrl = imageService.getArticleImageUrl(articlePo.getArticlePhoto());
        ArticleDetailPo articleDetailPo = articleDetailMapper.selectById(idxArticle);
        QueryWrapper<ArticleTagPo> wrapper = new QueryWrapper<>();
        wrapper.select("tag")
                .eq("idx_article",idxArticle)
                .ne("tag","");
        List<ArticleTagPo> tagList = articleTagMapper.selectList(wrapper);
        ArticleDetailVo articleDetailVo = ArticleDetailVo.convert(articlePo);
        articleDetailVo.setUserPhotoUrl(userPhotoUrl);
        articleDetailVo.setArticlePhotoUrl(articlePhotoUrl);
        articleDetailVo.setContent(articleDetailPo.getContent());
        articleDetailVo.setEditType(articleDetailPo.getEditType());
        articleDetailVo.setTagList(tagList);
        return ResVo.success(articleDetailVo);
    }

    /**
     * @param idxArticle 文章唯一 Id
     * @return 获取更新界面文章信息
     */
    public ResVo getArticleForUpdate(int idxArticle){
        ArticlePo articlePo = articleMapper.selectById(idxArticle);
        String userPhotoUrl = imageService.getUserImageUrl(articlePo.getUserPhoto());
        String articlePhotoUrl = imageService.getArticleImageUrl(articlePo.getArticlePhoto());
        ArticleDetailPo articleDetailPo = articleDetailMapper.selectById(idxArticle);
        QueryWrapper<ArticleTagPo> wrapper = new QueryWrapper<>();
        wrapper.select("id","tag")
                .eq("idx_article",idxArticle);
        List<ArticleTagPo> tagList = articleTagMapper.selectList(wrapper);
        ArticleDetailVo articleDetailVo = ArticleDetailVo.convert(articlePo);
        articleDetailVo.setUserPhotoUrl(userPhotoUrl);
        articleDetailVo.setArticlePhotoUrl(articlePhotoUrl);
        articleDetailVo.setContent(articleDetailPo.getContent());
        articleDetailVo.setEditType(articleDetailPo.getEditType());
        articleDetailVo.setTagList(tagList);
        articleDetailVo.setArticlePhoto(articlePo.getArticlePhoto());
        return ResVo.success(articleDetailVo);
    }

    /**
     * 对文章标题，子标题和小结部分进行关键字匹配，从es中获取id，再从mysql中获取源数据
     * @param key 关键字
     * @return 查询结果不为空返回文章列表，为空返回错误
     */
    public ResVo searchArticleByEs(String key){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(key,
                EsFieldConstant.ES_FIELD_TITLE,
                EsFieldConstant.ES_FIELD_SUBTITLE,
                EsFieldConstant.ES_FIELD_SUMMARY);
        searchSourceBuilder.query(multiMatchQueryBuilder);

        SearchRequest searchRequest = new SearchRequest(new String[]{EsIndexConstant.ES_INDEX_ARTICLE},
                searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hitsList = hits.getHits();
        List<Integer> ids = new ArrayList<>();
        for (SearchHit documentFields : hitsList) {
            ids.add(Integer.parseInt(documentFields.getId()));
        }
        if (ObjectUtils.isEmpty(ids)) {
            return ResVo.failure(ResStatusEnum.ERROR);
        }
        List<ArticlePo> records = articleMapper.selectBatchIds(ids);
        return ResVo.success(records);
    }

    public ResVo searchArticleByMysql(String key,int pageCurrent,int pageSize,int orderType){
        Page<ArticlePo> page = new Page<>(pageCurrent, pageSize);
        QueryWrapper<ArticlePo> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete",0)
                .like("title",key)
                .or()
                .like("subtitle",key)
                .or()
                .like("summary",key);
        switch (orderType){
            case 1:
                wrapper.orderBy(true,false,"article_like");
                break;
            case 2:
                wrapper.orderBy(true,false,"article_collect");
                break;
            case 3:
                wrapper.orderBy(true,false,"gmt_create");
                break;
            case 4:
                wrapper.orderBy(true,true,"article_view")
                        .orderBy(true,true,"article_like")
                        .orderBy(true,true,"gmt_create");
                break;
            case 5:
                wrapper.orderBy(true,true,"article_like");
                break;
            case 6:
                wrapper.orderBy(true,true,"article_collect");
                break;
            case 7:
                wrapper.orderBy(true,true,"gmt_create");
                break;
            default:
                wrapper.orderBy(true,false,"article_view")
                        .orderBy(true,false,"article_like")
                        .orderBy(true,false,"gmt_create");
        }
        page = articleMapper.selectPage(page,wrapper);
        List<ArticlePo> list = page.getRecords();
        List<ArticleVo> volist = new ArrayList<>();
        for(ArticlePo item : list){
            String articleImageUrl = imageService.getArticleImageUrl(item.getArticlePhoto());
            String userImageUrl = imageService.getUserImageUrl(item.getUserPhoto());
            ArticleVo articleVo = ArticleVo.convert(item);
            articleVo.setArticlePhotoUrl(articleImageUrl);
            articleVo.setUserPhotoUrl(userImageUrl);
            volist.add(articleVo);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("record",volist);
        map.put("total",page.getPages());
        return ResVo.success(map);
    }

    public ResVo insertArticleToEs(ArticleDTO articleDTO){
        ArticleDetailPo articleDetailPO = ArticleDetailPo.insert(articleDTO);
        ArticlePo articlePO = null;
        try {
            articlePO = ArticlePo.insert(articleDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int isArticleSuccess = articleMapper.insert(articlePO);
        int isArticleDetailSuccess = articleDetailMapper.insert(articleDetailPO);
        // 获取新插入文章的主键id
        int id = articlePO.getId();
        int isArticleTagSuccess = 0;
        if(isArticleDetailSuccess == 1 && isArticleSuccess == 1){
            String[] tagList = articleDTO.getTag();
            for(String tag : tagList){
                ArticleTagPo articleTagPO = ArticleTagPo.insert(id,tag,articleDTO.getGmtCreate());
                isArticleTagSuccess = articleTagMapper.insert(articleTagPO);
            }
        }
        // elasticsearch插入文章新数据
        IndexRequest indexRequest = new IndexRequest("article").id(Integer.toString(id));
        ArticleEsPo articleESPO = ArticleEsPo.insert(id,articleDTO);
        indexRequest.source(JSON.toJSONString(articleESPO), XContentType.JSON);
        int isArticleESSuccess = 1;
        try{
            restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
        }catch (IOException e){
            e.printStackTrace();
            isArticleESSuccess = 0;
        }
        if(isArticleTagSuccess == 1 && isArticleESSuccess == 1)
            return ResVo.success();
        else
            return ResVo.failure(ResStatusEnum.ERROR);
    }

    public ResVo insertArticleToMysql(ArticleDTO articleDTO) {
        //插入article表
        ArticlePo articlePO = new ArticlePo();
        try {
            articlePO = ArticlePo.insert(articleDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int isArticleSuccess = articleMapper.insert(articlePO);

        //插入article_detail表
        ArticleDetailPo articleDetailPO = ArticleDetailPo.insert(articleDTO);
        int isArticleDetailSuccess = articleDetailMapper.insert(articleDetailPO);

        // 获取新插入文章的主键id
        int id = articlePO.getId();

        //插入article_tag表
        int isArticleTagSuccess = 0;
        if (isArticleDetailSuccess == 1 && isArticleSuccess == 1) {
            String[] tagList = articleDTO.getTag();
            for (String tag : tagList) {
                ArticleTagPo articleTagPO = ArticleTagPo.insert(id, tag, articleDTO.getGmtCreate());
                isArticleTagSuccess = articleTagMapper.insert(articleTagPO);
            }
        }
        if (isArticleTagSuccess == 1) {
            //返回插入文章的id
            return ResVo.success(id);
        } else {
            return ResVo.failure();
        }
    }

    public ResVo updateArticleInMysql(ArticleDTO articleDTO){
        //更新文章
        UpdateWrapper<ArticlePo> articleWrapper = new UpdateWrapper<>();
        articleWrapper.eq("id",articleDTO.getId())
                .set("title",articleDTO.getTitle())
                .set("subtitle",articleDTO.getSubtitle())
                .set("summary",articleDTO.getSummary())
                .set("article_photo",articleDTO.getArticlePhoto())
                .set("gmt_modify",articleDTO.getGmtModify());
        int isArticleSuccess = articleMapper.update(null,articleWrapper);
        //更新文章内容
        UpdateWrapper<ArticleDetailPo> articleDetailWrapper = new UpdateWrapper<>();
        articleDetailWrapper.eq("id",articleDTO.getId())
                .set("content",articleDTO.getContent())
                .set("edit_type",articleDTO.getEditType())
                .set("gmt_modify",articleDTO.getGmtModify());
        int isArticleDetailSuccess = articleDetailMapper.update(null,articleDetailWrapper);
        //更新文章tag
        Map<Integer,String> tagmap = articleDTO.getUpdateTagMap();
        tagmap.forEach((id,tag)->{
                UpdateWrapper<ArticleTagPo> articleTagWrapper = new UpdateWrapper<>();
                articleTagWrapper.eq("id",id)
                        .set("tag",tag);
                articleTagMapper.update(null,articleTagWrapper);
        });
        if(isArticleDetailSuccess == 1 && isArticleSuccess == 1){
            return ResVo.success();
        }
        else
            return ResVo.failure();
    }

    public ResVo deleteArticleInMysql(int idxArticle){
        UpdateWrapper<ArticlePo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",idxArticle)
                .set("is_delete",1);
        int isArticleSuccess = articleMapper.update(null,wrapper);
        if(isArticleSuccess == 1){
            return ResVo.success();
        }
        else
            return ResVo.failure(ResStatusEnum.ERROR);
    }

    public ResVo updateArticleCommentMark(ArticleDTO articleDTO){
        UpdateWrapper<ArticlePo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",articleDTO.getId())
                .set("mark_data",articleDTO.getMarkData())
                .set("total_mark",articleDTO.getTotalMark());
        articleMapper.update(null,updateWrapper);
        return ResVo.success();
    }

}
