package com.easou.novel.crawl.command;

import java.util.List;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.easou.novel.crawl.constant.CommandContant;
import com.easou.novel.crawl.frontier.WorkQueueFrontier;
import com.easou.novel.crawl.model.CrawlUrl;
import com.easou.novel.crawl.result.Result;
import com.easou.novel.crawl.result.enumtype.CommandEnum;
import com.easou.novel.crawl.result.enumtype.FailureStatusEnum;
import com.easou.novel.crawl.task.CrawlTask;

@Component("postProcessorCommand")
public class PostProcessorCommand implements Command {
	private static Logger logger = LoggerFactory.getLogger(PostProcessorCommand.class);
	
	@Autowired
	private WorkQueueFrontier workQueueFrontier;
	
	@Override@SuppressWarnings("unchecked")
	public boolean execute(Context context) throws Exception {
		List<CrawlUrl> catalogUrls = (List<CrawlUrl>) context.get(CommandContant.CATALOG_URL_LIST);
		int size = 0;
		if(catalogUrls != null){
			size = catalogUrls.size();
			for(CrawlUrl url : catalogUrls){
//				logger.info("putQueue: url[{}] type[{}] rankOrder[{}]", url.getUri().toString(), url.getType(), url.getRankOrder());
				logger.info(url.getUri().toString() + " putQueue: type[{}] rankOrder[{}]", url.getType(), url.getRankOrder() );
				workQueueFrontier.schedule(url);
			}
		} else {
		    logger.info("catalogUrls is null");
		}
		Result result = (Result)context.get(CommandContant.COMMAND_RESULT);
		result.setSuccess(true);
		result.getModel().put("model", CommandEnum.POST_PROCESSOR.getName());
		result.getModel().put("msg", "add crawl url size:" + size);
		return false;
	}
}
