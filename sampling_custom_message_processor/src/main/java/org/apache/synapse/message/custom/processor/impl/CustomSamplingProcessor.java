package org.apache.synapse.message.custom.processor.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.RegistryException;
import org.wso2.carbon.registry.api.Resource;

import org.apache.synapse.message.processor.impl.sampler.SamplingProcessor;

public class CustomSamplingProcessor extends SamplingProcessor {
	private final static String REGISTRY_PATH = "reg";

	@Override
	public void setParameters(Map<String, Object> parameters) {
		/*
		if (parameters.get(REGISTRY_PATH) != null) {
			String resourcePath = (String) parameters.get(REGISTRY_PATH);
			parameters = populateParamsFromReg(resourcePath, parameters);
		}
		*/
		String resourcePath = "NotUsed";
		parameters = populateParamsFromReg(resourcePath, parameters);
		System.out.println("############################## set params################");
		super.setParameters(parameters);
	}

	private Map<String, Object> populateParamsFromReg(String resourcePath, Map<String, Object> parameters) {
		/*
		Properties prop = readPropertyFile(resourcePath);
		if (prop != null) {
			for (String key : prop.stringPropertyNames()) {
				String value = prop.getProperty(key);
				parameters.put(key, value);
			}
		}*/
		String value_interval = System.getenv("mp_interval");
		String value_concurrency = System.getenv("mp_concurrency");
		String value_sequence = System.getenv("mp_sequence");
		String value_is_active = System.getenv("mp_is_active");
		
		String key_interval = "interval";
		String key_concurrency = "concurrency";
		String key_sequence = "sequence";
		String key_is_active = "is.active";
		
		parameters.put(key_interval, value_interval);
		parameters.put(key_concurrency, value_concurrency);
		parameters.put(key_sequence, value_sequence);
		parameters.put(key_is_active, value_is_active);				
		
		return parameters;
	}

	private Properties readPropertyFile(String resourcePath) {
		Properties prop = null;
		try {
			CarbonContext cCtx = CarbonContext.getThreadLocalCarbonContext();
			Registry registry = cCtx.getRegistry(RegistryType.USER_GOVERNANCE);
			Resource resource = registry.get(resourcePath);
			Object content = resource.getContent();
			String output = new String((byte[]) content);
			System.out.println(output);
			prop = parseProperties(output);
		} catch (RegistryException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
	}

	public Properties parseProperties(String fileContent) throws IOException {
		final Properties properties = new Properties();
		properties.load(new StringReader(fileContent));
		return properties;
	}
}
