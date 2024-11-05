
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 闲置物品订单
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/shangpinOrder")
public class ShangpinOrderController {
    private static final Logger logger = LoggerFactory.getLogger(ShangpinOrderController.class);

    private static final String TABLE_NAME = "shangpinOrder";

    @Autowired
    private ShangpinOrderService shangpinOrderService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ShangpinCommentbackService shangpinCommentbackService;
    //级联表非注册的service
    @Autowired
    private AddressService addressService;
    @Autowired
    private ShangpinService shangpinService;
    //注册表service
    @Autowired
    private YonghuService yonghuService;


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = shangpinOrderService.queryPage(params);

        //字典表数据转换
        List<ShangpinOrderView> list =(List<ShangpinOrderView>)page.getList();
        for(ShangpinOrderView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ShangpinOrderEntity shangpinOrder = shangpinOrderService.selectById(id);
        if(shangpinOrder !=null){
            //entity转view
            ShangpinOrderView view = new ShangpinOrderView();
            BeanUtils.copyProperties( shangpinOrder , view );//把实体数据重构到view中
            //级联表 收货地址
            //级联表
            AddressEntity address = addressService.selectById(shangpinOrder.getAddressId());
            if(address != null){
            BeanUtils.copyProperties( address , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setAddressId(address.getId());
            }
            //级联表 闲置物品
            //级联表
            ShangpinEntity shangpin = shangpinService.selectById(shangpinOrder.getShangpinId());
            if(shangpin != null){
            BeanUtils.copyProperties( shangpin , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setShangpinId(shangpin.getId());
            }
            //级联表 用户
            //级联表
            YonghuEntity yonghu = yonghuService.selectById(shangpinOrder.getYonghuId());
            if(yonghu != null){
            BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setYonghuId(yonghu.getId());
            }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ShangpinOrderEntity shangpinOrder, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,shangpinOrder:{}",this.getClass().getName(),shangpinOrder.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");
        else if("用户".equals(role))
            shangpinOrder.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        shangpinOrder.setCreateTime(new Date());
        shangpinOrder.setInsertTime(new Date());
        shangpinOrderService.insert(shangpinOrder);

        return R.ok();
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ShangpinOrderEntity shangpinOrder, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,shangpinOrder:{}",this.getClass().getName(),shangpinOrder.toString());
        ShangpinOrderEntity oldShangpinOrderEntity = shangpinOrderService.selectById(shangpinOrder.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
//        else if("用户".equals(role))
//            shangpinOrder.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));
        //根据字段查询是否有相同数据
        Wrapper<ShangpinOrderEntity> queryWrapper = new EntityWrapper<ShangpinOrderEntity>()
            .eq("id",0)
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ShangpinOrderEntity shangpinOrderEntity = shangpinOrderService.selectOne(queryWrapper);
        if(shangpinOrderEntity==null){
            shangpinOrderService.updateById(shangpinOrder);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<ShangpinOrderEntity> oldShangpinOrderList =shangpinOrderService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        shangpinOrderService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<ShangpinOrderEntity> shangpinOrderList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            ShangpinOrderEntity shangpinOrderEntity = new ShangpinOrderEntity();
//                            shangpinOrderEntity.setShangpinOrderUuidNumber(data.get(0));                    //订单号 要改的
//                            shangpinOrderEntity.setAddressId(Integer.valueOf(data.get(0)));   //收货地址 要改的
//                            shangpinOrderEntity.setShangpinId(Integer.valueOf(data.get(0)));   //闲置物品 要改的
//                            shangpinOrderEntity.setYonghuId(Integer.valueOf(data.get(0)));   //用户 要改的
//                            shangpinOrderEntity.setBuyNumber(Integer.valueOf(data.get(0)));   //购买数量 要改的
//                            shangpinOrderEntity.setShangpinOrderTruePrice(data.get(0));                    //实付价格 要改的
//                            shangpinOrderEntity.setShangpinOrderTypes(Integer.valueOf(data.get(0)));   //订单类型 要改的
//                            shangpinOrderEntity.setShangpinOrderPaymentTypes(Integer.valueOf(data.get(0)));   //支付类型 要改的
//                            shangpinOrderEntity.setInsertTime(date);//时间
//                            shangpinOrderEntity.setCreateTime(date);//时间
                            shangpinOrderList.add(shangpinOrderEntity);


                            //把要查询是否重复的字段放入map中
                                //订单号
                                if(seachFields.containsKey("shangpinOrderUuidNumber")){
                                    List<String> shangpinOrderUuidNumber = seachFields.get("shangpinOrderUuidNumber");
                                    shangpinOrderUuidNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> shangpinOrderUuidNumber = new ArrayList<>();
                                    shangpinOrderUuidNumber.add(data.get(0));//要改的
                                    seachFields.put("shangpinOrderUuidNumber",shangpinOrderUuidNumber);
                                }
                        }

                        //查询是否重复
                         //订单号
                        List<ShangpinOrderEntity> shangpinOrderEntities_shangpinOrderUuidNumber = shangpinOrderService.selectList(new EntityWrapper<ShangpinOrderEntity>().in("shangpin_order_uuid_number", seachFields.get("shangpinOrderUuidNumber")));
                        if(shangpinOrderEntities_shangpinOrderUuidNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(ShangpinOrderEntity s:shangpinOrderEntities_shangpinOrderUuidNumber){
                                repeatFields.add(s.getShangpinOrderUuidNumber());
                            }
                            return R.error(511,"数据库的该表中的 [订单号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        shangpinOrderService.insertBatch(shangpinOrderList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }





    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = shangpinOrderService.queryPage(params);

        //字典表数据转换
        List<ShangpinOrderView> list =(List<ShangpinOrderView>)page.getList();
        for(ShangpinOrderView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ShangpinOrderEntity shangpinOrder = shangpinOrderService.selectById(id);
            if(shangpinOrder !=null){


                //entity转view
                ShangpinOrderView view = new ShangpinOrderView();
                BeanUtils.copyProperties( shangpinOrder , view );//把实体数据重构到view中

                //级联表
                    AddressEntity address = addressService.selectById(shangpinOrder.getAddressId());
                if(address != null){
                    BeanUtils.copyProperties( address , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setAddressId(address.getId());
                }
                //级联表
                    ShangpinEntity shangpin = shangpinService.selectById(shangpinOrder.getShangpinId());
                if(shangpin != null){
                    BeanUtils.copyProperties( shangpin , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setShangpinId(shangpin.getId());
                }
                //级联表
                    YonghuEntity yonghu = yonghuService.selectById(shangpinOrder.getYonghuId());
                if(yonghu != null){
                    BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setYonghuId(yonghu.getId());
                }
                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody ShangpinOrderEntity shangpinOrder, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,shangpinOrder:{}",this.getClass().getName(),shangpinOrder.toString());
            ShangpinEntity shangpinEntity = shangpinService.selectById(shangpinOrder.getShangpinId());
            if(shangpinEntity == null){
                return R.error(511,"查不到该闲置物品");
            }
            if(false){
            }
            else if(shangpinEntity.getShangpinNewMoney() == null){
                return R.error(511,"现价不能为空");
            }
            else if((shangpinEntity.getShangpinKucunNumber() -shangpinOrder.getBuyNumber())<0){
                return R.error(511,"购买数量不能大于库存数量");
            }

            YonghuEntity yonghuEntity1 = yonghuService.selectById(shangpinEntity.getYonghuId());


        //计算所获得积分
            Double buyJifen =0.0;
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            YonghuEntity yonghuEntity = yonghuService.selectById(userId);
            if(yonghuEntity == null)
                return R.error(511,"用户不能为空");
            if(yonghuEntity.getNewMoney() == null)
                return R.error(511,"用户金额不能为空");
            double balance = yonghuEntity.getNewMoney() - shangpinEntity.getShangpinNewMoney()*shangpinOrder.getBuyNumber();//余额

            yonghuEntity1.setNewMoney(shangpinEntity.getShangpinNewMoney()*shangpinOrder.getBuyNumber());

            if(balance<0)
                return R.error(511,"余额不够支付");
            shangpinOrder.setShangpinOrderTypes(101); //设置订单状态为已支付
            shangpinOrder.setShangpinOrderTruePrice(shangpinEntity.getShangpinNewMoney()*shangpinOrder.getBuyNumber()); //设置实付价格
            shangpinOrder.setYonghuId(userId); //设置订单支付人id
            shangpinOrder.setShangpinOrderUuidNumber(String.valueOf(new Date().getTime()));
            shangpinOrder.setShangpinOrderPaymentTypes(1);
            shangpinOrder.setInsertTime(new Date());
            shangpinOrder.setCreateTime(new Date());
                shangpinEntity.setShangpinKucunNumber( shangpinEntity.getShangpinKucunNumber() -shangpinOrder.getBuyNumber());
                shangpinService.updateById(shangpinEntity);
                shangpinOrderService.insert(shangpinOrder);//新增订单
                yonghuEntity.setNewMoney(balance);//设置金额
            yonghuService.updateById(yonghuEntity);
        yonghuService.updateById(yonghuEntity1);

            return R.ok();
    }


    /**
    * 退款
    */
    @RequestMapping("/refund")
    public R refund(Integer id, HttpServletRequest request){
        logger.debug("refund方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        String role = String.valueOf(request.getSession().getAttribute("role"));

            ShangpinOrderEntity shangpinOrder = shangpinOrderService.selectById(id);
            Integer buyNumber = shangpinOrder.getBuyNumber();
            Integer shangpinOrderPaymentTypes = shangpinOrder.getShangpinOrderPaymentTypes();
            Integer shangpinId = shangpinOrder.getShangpinId();
            if(shangpinId == null)
                return R.error(511,"查不到该闲置物品");
            ShangpinEntity shangpinEntity = shangpinService.selectById(shangpinId);
            YonghuEntity yonghuEntity1 = yonghuService.selectById(shangpinEntity.getYonghuId());
        if(shangpinEntity == null)
                return R.error(511,"查不到该闲置物品");
            Double shangpinNewMoney = shangpinEntity.getShangpinNewMoney();
            if(shangpinNewMoney == null)
                return R.error(511,"闲置物品价格不能为空");

            Integer userId = (Integer) request.getSession().getAttribute("userId");
            YonghuEntity yonghuEntity = yonghuService.selectById(userId);
            if(yonghuEntity == null)
                return R.error(511,"用户不能为空");
            if(yonghuEntity.getNewMoney() == null)
                return R.error(511,"用户金额不能为空");

            Double zhekou = 1.0;


            //判断是什么支付方式 1代表余额 2代表积分
            if(shangpinOrderPaymentTypes == 1){//余额支付
                //计算金额
                Double money = shangpinEntity.getShangpinNewMoney() * buyNumber  * zhekou;
                //计算所获得积分
                Double buyJifen = 0.0;
                yonghuEntity.setNewMoney(yonghuEntity.getNewMoney() + money); //设置金额
                yonghuEntity1.setNewMoney(yonghuEntity1.getNewMoney() - money);

            }

            shangpinEntity.setShangpinKucunNumber(shangpinEntity.getShangpinKucunNumber() + buyNumber);



            shangpinOrder.setShangpinOrderTypes(102);//设置订单状态为退款
            shangpinOrderService.updateById(shangpinOrder);//根据id更新
            yonghuService.updateById(yonghuEntity);//更新用户信息
            yonghuService.updateById(yonghuEntity1);//更新用户信息
            shangpinService.updateById(shangpinEntity);//更新订单中闲置物品的信息

            return R.ok();
    }

    /**
    * 评价
    */
    @RequestMapping("/commentback")
    public R commentback(Integer id, String commentbackText, Integer shangpinCommentbackPingfenNumber, HttpServletRequest request){
        logger.debug("commentback方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
            ShangpinOrderEntity shangpinOrder = shangpinOrderService.selectById(id);
        if(shangpinOrder == null)
            return R.error(511,"查不到该订单");
        Integer shangpinId = shangpinOrder.getShangpinId();
        if(shangpinId == null)
            return R.error(511,"查不到该闲置物品");

        ShangpinCommentbackEntity shangpinCommentbackEntity = new ShangpinCommentbackEntity();
            shangpinCommentbackEntity.setId(id);
            shangpinCommentbackEntity.setShangpinId(shangpinId);
            shangpinCommentbackEntity.setYonghuId((Integer) request.getSession().getAttribute("userId"));
            shangpinCommentbackEntity.setShangpinCommentbackText(commentbackText);
            shangpinCommentbackEntity.setInsertTime(new Date());
            shangpinCommentbackEntity.setReplyText(null);
            shangpinCommentbackEntity.setUpdateTime(null);
            shangpinCommentbackEntity.setCreateTime(new Date());
            shangpinCommentbackService.insert(shangpinCommentbackEntity);

            shangpinOrder.setShangpinOrderTypes(105);//设置订单状态为已评价
            shangpinOrderService.updateById(shangpinOrder);//根据id更新
            return R.ok();
    }

    /**
     * 发货
     */
    @RequestMapping("/deliver")
    public R deliver(Integer id ){
        logger.debug("refund:,,Controller:{},,ids:{}",this.getClass().getName(),id.toString());
        ShangpinOrderEntity  shangpinOrderEntity = shangpinOrderService.selectById(id);
        shangpinOrderEntity.setShangpinOrderTypes(103);//设置订单状态为已发货
        boolean b =  shangpinOrderService.updateById( shangpinOrderEntity);

        if(!b){
            return R.error("发货出错");
        }
        return R.ok();
    }


    /**
     * 收货
     */
    @RequestMapping("/receiving")
    public R receiving(Integer id){
        logger.debug("refund:,,Controller:{},,ids:{}",this.getClass().getName(),id.toString());
        ShangpinOrderEntity  shangpinOrderEntity = shangpinOrderService.selectById(id);
        shangpinOrderEntity.setShangpinOrderTypes(104);//设置订单状态为收货
        boolean b =  shangpinOrderService.updateById( shangpinOrderEntity);
        if(!b){
            return R.error("收货出错");
        }
        return R.ok();
    }

}
