package com.jiuzhang.seckill.web;


import com.jiuzhang.seckill.db.dao.SeckillActivityDao;
import com.jiuzhang.seckill.db.dao.SeckillCommodityDao;
import com.jiuzhang.seckill.db.po.SeckillActivity;
import com.jiuzhang.seckill.db.po.SeckillCommodity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class SeckillActivityController {
    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private SeckillCommodityDao seckillCommodityDao;

    /**
     * 查询秒杀活动的列表
     *
     * @param resultMap
     * @return
     */
    @RequestMapping("/seckills")
    public String sucessTest(Map<String,Object> resultMap){
        List<SeckillActivity> seckillActivities = seckillActivityDao.querySeckillActivitysByStatus(1);
        resultMap.put("seckillActivities",seckillActivities);
        return "seckill_activity";
    }

    /**
     * 秒杀商品详情
     * @param resultMap
     * @param seckillActivityId
     * @return
     */
    @RequestMapping("/item/{seckillActivityId}")
    public String itemPage(Map<String,Object> resultMap,@PathVariable long seckillActivityId){
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        SeckillCommodity seckillCommodity = seckillCommodityDao.querySeckillCommodityById(seckillActivity.getCommodityId());

        resultMap.put("seckillActivity",seckillActivity);
        resultMap.put("seckillCommodity",seckillCommodity);
        resultMap.put("seckillPrice",seckillActivity.getSeckillPrice());
        resultMap.put("oldPrice",seckillActivity.getOldPrice());
        resultMap.put("commodityId",seckillActivity.getCommodityId());
        resultMap.put("commodityName",seckillCommodity.getCommodityName());
        resultMap.put("commodityDesc",seckillCommodity.getCommodityDesc());
        return "seckill_item";
    }

    @ResponseBody
    @RequestMapping("/addSeckillActivityAction")
    public String addSeckillActivityAction(
            @RequestParam("name") String name,
            @RequestParam("commodityId") long commodityId,
            @RequestParam("seckillPrice") BigDecimal seckillPrice,
            @RequestParam("oldPrice") BigDecimal oldPrice,
            @RequestParam("seckillNumber") long seckillNumber
    ) {
        SeckillActivity seckillActivity = new SeckillActivity();
        seckillActivity.setName(name);
        seckillActivity.setCommodityId(commodityId);
        seckillActivity.setSeckillPrice(seckillPrice);
        seckillActivity.setOldPrice(oldPrice);
        seckillActivity.setTotalStock(seckillNumber);
        seckillActivity.setAvailableStock(seckillNumber);
        seckillActivity.setLockStock(0L);
        seckillActivity.setActivityStatus(1);
        seckillActivityDao.inertSeckillActivity(seckillActivity);
        return seckillActivity.toString();
    }

    @RequestMapping("/addSeckillActivity")
    public String addSeckillActivity(){
        return "add_activity";
    }
}
