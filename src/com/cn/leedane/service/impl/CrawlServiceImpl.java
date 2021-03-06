package com.cn.leedane.service.impl;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.leedane.mapper.CrawlMapper;
import com.cn.leedane.model.CrawlBean;
import com.cn.leedane.service.CrawlService;

/**
 * 爬虫的service实现类
 * @author LeeDane
 * 2016年7月12日 下午1:32:58
 * Version 1.0
 */
@Service("crawlService")
public class CrawlServiceImpl implements CrawlService<CrawlBean> {
	Logger logger = Logger.getLogger(getClass());
	@Autowired
	private CrawlMapper crawlMapper;

	@Override
	public List<CrawlBean> findAllNotCrawl(int limit, String source) {
		return this.crawlMapper.findAllNotCrawl(limit, source);
	}

	@Override
	public List<CrawlBean> findAllHotNotCrawl(int limit) {
		return this.crawlMapper.findAllHotNotCrawl(limit);
	}

	@Override
	public boolean updateAllScore() {
		return this.crawlMapper.updateAllScore();
	}

	@Override
	public boolean save(CrawlBean t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(CrawlBean t) {
		// TODO Auto-generated method stub
		return false;
	}
}
