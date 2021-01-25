package com.pingan.life.micromall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pingan.life.common.jsr303.IntegerValueDomain;
import com.pingan.life.common.jsr303.ParamCheckGroupForSave;
import com.pingan.life.common.jsr303.ParamCheckGroupForUpdate;
import com.pingan.life.common.jsr303.ParamCheckGroupForUpdateStatus;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 * 
 * @author zhaopeng
 * @email zpen_wy@163.com
 * @date 2020-12-23 01:00:49
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "修改操作品牌ID不能为空", groups = {ParamCheckGroupForUpdate.class, ParamCheckGroupForUpdateStatus.class})
	@Null(message = "新增操作不需要指定品牌ID", groups = ParamCheckGroupForSave.class)
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名称不能为空", groups = {ParamCheckGroupForSave.class, ParamCheckGroupForUpdate.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "新增操作品牌LOGO不能为空", groups = ParamCheckGroupForSave.class)
	@URL(message = "品牌LOGO必须是一个合法的URL地址", groups = {ParamCheckGroupForSave.class, ParamCheckGroupForUpdate.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "显示状态不能为空", groups = {ParamCheckGroupForSave.class, ParamCheckGroupForUpdateStatus.class})
	@IntegerValueDomain(value = {0, 1}, groups = {ParamCheckGroupForSave.class, ParamCheckGroupForUpdate.class, ParamCheckGroupForUpdateStatus.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotNull(message = "新增操作检索首字母不能为空", groups = {ParamCheckGroupForSave.class})
	@Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是一个英文字母", groups = {ParamCheckGroupForSave.class, ParamCheckGroupForUpdate.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "新增操作排序字段不能为空", groups = {ParamCheckGroupForSave.class})
	@Min(value = 0, message = "排序字段必须是一个正整数", groups = {ParamCheckGroupForSave.class, ParamCheckGroupForUpdate.class})
	private Integer sort;

}
