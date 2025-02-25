package com.urise.webapp.util;

import com.urise.webapp.model.Periods;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class PeriodUtil {
    public static void writePeriods(DataOutputStream dos, List<Periods> periods) throws IOException {
        dos.writeInt(periods.size());
        for (Periods period : periods) {
            dos.writeUTF(period.getTitle());
            dos.writeUTF(period.getStartDate().toString());
            dos.writeUTF(period.getEndDate().toString());
            dos.writeUTF(period.getDescription());
        }
    }

    public static List<Periods> readPeriods(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        List<Periods> periods = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            periods.add(new Periods(
                    dis.readUTF(),
                    LocalDate.parse(dis.readUTF()),
                    LocalDate.parse(dis.readUTF()),
                    dis.readUTF()));
        }
        return periods;
    }
}
