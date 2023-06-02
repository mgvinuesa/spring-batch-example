package com.example.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.example.batch.repository.entity.Coffee;

public class CoffeeItemProcessor implements ItemProcessor<Coffee, Coffee> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoffeeItemProcessor.class);

	@Override
	public Coffee process(final Coffee coffee) throws Exception {
		Long id = coffee.getCoffee_id();
		String brand = coffee.getBrand().toUpperCase();
		String origin = coffee.getOrigin().toUpperCase();
		String chracteristics = coffee.getCharacteristics().toUpperCase();
		String processed = coffee.getProcessed();

		Coffee transformedCoffee = new Coffee(id, brand, origin, chracteristics, processed);
		transformedCoffee.setProcessed("true");
		LOGGER.info("Converting ( {} ) into ( {} )", coffee, transformedCoffee);

		return transformedCoffee;
	}
}