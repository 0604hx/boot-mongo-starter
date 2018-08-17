package org.nerve.core.module.cache;

public class CacheBean{
	String name;
	String spec;

	public String getName() {
		return name;
	}

	public CacheBean setName(String name) {
		this.name = name;
		return this;
	}

	public String getSpec() {
		return spec;
	}

	public CacheBean setSpec(String spec) {
		this.spec = spec;
		return this;
	}

	@Override
	public String toString() {
		return "{" +
				"name='" + name + '\'' +
				", spec='" + spec + '\'' +
				'}';
	}
}
