package org.nerve.domain;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * com.zeus.domain
 * Created by zengxm on 2017/8/22.
 */
abstract public class IdEntity implements Serializable, ID{

	@Id
	protected String id;

	/**
	 * 判断此对象是否存在有效的id
	 * @return
	 */
	public boolean using(){
		return id != null && id.length()>0;
	}

	/**
	 * Kotlin 版本：
	 * override fun equals(other: Any?): Boolean =
		 when (other){
			 this -> true
			 null -> false
			 other is IdEntity && this::class != other::class -> false
			 other is IdEntity && other.id == null -> false
			 other is IdEntity && other.id.equals(id) -> true
			 else -> false
		 }
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(getClass().getPackage() != obj.getClass().getPackage())
			return false;

		final IdEntity other = (IdEntity)obj;
		return id!=null && id.equals(other.id());
	}

	public String id() {
		return id;
	}

	public String getId() {
		return id;
	}

	public void setId(String  id) {
		this.id = id;
	}
}
