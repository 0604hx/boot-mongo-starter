package org.nerve.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.domain
 * FILE			ExtraEntity.java
 * Created by 	zengxm on 2017/12/20.
 */
public class ExtraEntity extends IdEntity {
	protected Map<String,Object> extras;

	public Map getExtras() {
		return extras;
	}

	public ExtraEntity setExtras(Map extras) {
		this.extras = extras;
		return this;
	}

	public ExtraEntity addExtra(String k, Object v){
		if(extras==null)
			extras = new HashMap();
		extras.put(k, v);
		return this;
	}

	public ExtraEntity rmExtra(String k){
		if(extras != null)
			extras.remove(k);
		return this;
	}

	public Object getExtra(String k){
		return extras==null?null:extras.get(k);
	}

	public String getStringExtra(String k,String dv){
		if(extras != null)
			return extras.getOrDefault(k, dv).toString();
		return dv;
	}

	public boolean getBoolExtra(String k, boolean dv){
		if(extras != null && extras.containsKey(k))
			return Boolean.valueOf(extras.get(k).toString());
		return dv;
	}

	public int getIntExtra(String k, int dv){
		if(extras != null && extras.containsKey(k))
			return Integer.valueOf(extras.get(k).toString());
		return dv;
	}
}
