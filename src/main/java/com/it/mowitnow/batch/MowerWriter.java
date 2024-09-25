package com.it.mowitnow.batch;

import com.it.mowitnow.model.Mower;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class MowerWriter implements ItemWriter<Mower> {
    @Override
    public void write(Chunk<? extends Mower> chunk) throws Exception {
        chunk.getItems().forEach(MowerWriter::printMowerStatus);
    }

    private static void printMowerStatus(Mower mower) {
        String status = String.format("%s %s %s", mower.getPosition().x(), mower.getPosition().y(), mower.getDirection());
        System.out.println(status);
    }
}
