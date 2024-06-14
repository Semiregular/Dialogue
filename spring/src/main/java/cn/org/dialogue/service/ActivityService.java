package cn.org.dialogue.service;

import cn.org.dialogue.dto.PageDTO;
import cn.org.dialogue.mapper.ActivityMapper;
import cn.org.dialogue.mapper.ArticleActivityMapper;
import cn.org.dialogue.mapper.ArticleMapper;
import cn.org.dialogue.po.ArticleActivityPo;
import cn.org.dialogue.po.ArticlePo;
import cn.org.dialogue.res.ResVo;
import cn.org.dialogue.vo.ArticleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 周正明
 * @date 2024/3/7
 */
@Service
@Component
public class ActivityService {
    @Autowired
    public ActivityMapper activityMapper;
    @Autowired
    public ArticleActivityMapper articleActivityMapper;
    @Autowired
    public ArticleMapper articleMapper;
    @Autowired
    public ImageService imageService;

    public int getArticleActivityTotal(PageDTO pageDTO){
        QueryWrapper<ArticleActivityPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("idx_activity",pageDTO.getId());
        int total = articleActivityMapper.selectCount(queryWrapper).intValue();
        int totalPage = (total + pageDTO.getPageSize() - 1)/pageDTO.getPageSize();
        return totalPage;
    }
    public ResVo listArticleActivity(PageDTO pageDTO){
        pageDTO.setStart();
        int total = getArticleActivityTotal(pageDTO);
        List<ArticlePo> list = articleActivityMapper.listArticleByActivityId(pageDTO);
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
        map.put("total",total);
        return ResVo.success(map);
    }
}
