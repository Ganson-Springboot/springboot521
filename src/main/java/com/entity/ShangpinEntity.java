package com.entity;

import com.annotation.ColumnInfo;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.*;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.utils.DateUtil;


/**
 * 闲置物品
 *
 * @author 
 * @email
 */
@TableName("shangpin")
public class ShangpinEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public ShangpinEntity() {

	}

	public ShangpinEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment="主键",type="int(11)")
    @TableField(value = "id")

    private Integer id;


    /**
     * 闲置物品名称
     */
    @ColumnInfo(comment="闲置物品名称",type="varchar(200)")
    @TableField(value = "shangpin_name")

    private String shangpinName;


    /**
     * 闲置物品照片
     */
    @ColumnInfo(comment="闲置物品照片",type="varchar(200)")
    @TableField(value = "shangpin_photo")

    private String shangpinPhoto;


    /**
     * 用户
     */
    @ColumnInfo(comment="用户",type="int(11)")
    @TableField(value = "yonghu_id")

    private Integer yonghuId;


    /**
     * 物品类型
     */
    @ColumnInfo(comment="物品类型",type="int(11)")
    @TableField(value = "shangpin_types")

    private Integer shangpinTypes;


    /**
     * 闲置物品库存
     */
    @ColumnInfo(comment="闲置物品库存",type="int(11)")
    @TableField(value = "shangpin_kucun_number")

    private Integer shangpinKucunNumber;


    /**
     * 原价
     */
    @ColumnInfo(comment="原价",type="decimal(10,2)")
    @TableField(value = "shangpin_old_money")

    private Double shangpinOldMoney;


    /**
     * 现价
     */
    @ColumnInfo(comment="现价",type="decimal(10,2)")
    @TableField(value = "shangpin_new_money")

    private Double shangpinNewMoney;


    /**
     * 点击次数
     */
    @ColumnInfo(comment="点击次数",type="int(11)")
    @TableField(value = "shangpin_clicknum")

    private Integer shangpinClicknum;


    /**
     * 是否上架
     */
    @ColumnInfo(comment="是否上架",type="int(11)")
    @TableField(value = "shangxia_types")

    private Integer shangxiaTypes;


    /**
     * 逻辑删除
     */
    @ColumnInfo(comment="逻辑删除",type="int(11)")
    @TableField(value = "shangpin_delete")

    private Integer shangpinDelete;


    /**
     * 闲置物品简介
     */
    @ColumnInfo(comment="闲置物品简介",type="text")
    @TableField(value = "shangpin_content")

    private String shangpinContent;


    /**
     * 创建时间     homeMain
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }
    /**
	 * 设置：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：闲置物品名称
	 */
    public String getShangpinName() {
        return shangpinName;
    }
    /**
	 * 设置：闲置物品名称
	 */

    public void setShangpinName(String shangpinName) {
        this.shangpinName = shangpinName;
    }
    /**
	 * 获取：闲置物品照片
	 */
    public String getShangpinPhoto() {
        return shangpinPhoto;
    }
    /**
	 * 设置：闲置物品照片
	 */

    public void setShangpinPhoto(String shangpinPhoto) {
        this.shangpinPhoto = shangpinPhoto;
    }
    /**
	 * 获取：用户
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }
    /**
	 * 设置：用户
	 */

    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 获取：物品类型
	 */
    public Integer getShangpinTypes() {
        return shangpinTypes;
    }
    /**
	 * 设置：物品类型
	 */

    public void setShangpinTypes(Integer shangpinTypes) {
        this.shangpinTypes = shangpinTypes;
    }
    /**
	 * 获取：闲置物品库存
	 */
    public Integer getShangpinKucunNumber() {
        return shangpinKucunNumber;
    }
    /**
	 * 设置：闲置物品库存
	 */

    public void setShangpinKucunNumber(Integer shangpinKucunNumber) {
        this.shangpinKucunNumber = shangpinKucunNumber;
    }
    /**
	 * 获取：原价
	 */
    public Double getShangpinOldMoney() {
        return shangpinOldMoney;
    }
    /**
	 * 设置：原价
	 */

    public void setShangpinOldMoney(Double shangpinOldMoney) {
        this.shangpinOldMoney = shangpinOldMoney;
    }
    /**
	 * 获取：现价
	 */
    public Double getShangpinNewMoney() {
        return shangpinNewMoney;
    }
    /**
	 * 设置：现价
	 */

    public void setShangpinNewMoney(Double shangpinNewMoney) {
        this.shangpinNewMoney = shangpinNewMoney;
    }
    /**
	 * 获取：点击次数
	 */
    public Integer getShangpinClicknum() {
        return shangpinClicknum;
    }
    /**
	 * 设置：点击次数
	 */

    public void setShangpinClicknum(Integer shangpinClicknum) {
        this.shangpinClicknum = shangpinClicknum;
    }
    /**
	 * 获取：是否上架
	 */
    public Integer getShangxiaTypes() {
        return shangxiaTypes;
    }
    /**
	 * 设置：是否上架
	 */

    public void setShangxiaTypes(Integer shangxiaTypes) {
        this.shangxiaTypes = shangxiaTypes;
    }
    /**
	 * 获取：逻辑删除
	 */
    public Integer getShangpinDelete() {
        return shangpinDelete;
    }
    /**
	 * 设置：逻辑删除
	 */

    public void setShangpinDelete(Integer shangpinDelete) {
        this.shangpinDelete = shangpinDelete;
    }
    /**
	 * 获取：闲置物品简介
	 */
    public String getShangpinContent() {
        return shangpinContent;
    }
    /**
	 * 设置：闲置物品简介
	 */

    public void setShangpinContent(String shangpinContent) {
        this.shangpinContent = shangpinContent;
    }
    /**
	 * 获取：创建时间     homeMain
	 */
    public Date getCreateTime() {
        return createTime;
    }
    /**
	 * 设置：创建时间     homeMain
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Shangpin{" +
            ", id=" + id +
            ", shangpinName=" + shangpinName +
            ", shangpinPhoto=" + shangpinPhoto +
            ", yonghuId=" + yonghuId +
            ", shangpinTypes=" + shangpinTypes +
            ", shangpinKucunNumber=" + shangpinKucunNumber +
            ", shangpinOldMoney=" + shangpinOldMoney +
            ", shangpinNewMoney=" + shangpinNewMoney +
            ", shangpinClicknum=" + shangpinClicknum +
            ", shangxiaTypes=" + shangxiaTypes +
            ", shangpinDelete=" + shangpinDelete +
            ", shangpinContent=" + shangpinContent +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}
