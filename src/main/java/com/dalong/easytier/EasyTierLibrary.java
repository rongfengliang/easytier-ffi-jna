package com.dalong.easytier;

import com.sun.jna.*;
import com.sun.jna.ptr.PointerByReference;

import java.util.Arrays;
import java.util.List;

public interface EasyTierLibrary extends Library {

    class KeyValuePair extends Structure {
        public static class ByReference extends KeyValuePair implements Structure.ByReference {}

        public Pointer key;
        public Pointer value;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("key", "value");
        }
    }

    EasyTierLibrary INSTANCE = Native.load(System.getProperty("easytier.library.path", "easytier_ffi"),
            EasyTierLibrary.class);

    void get_error_msg(PointerByReference out);
    void free_string(Pointer s);
    int parse_config(String cfg_str);
    int run_network_instance(String cfg_str);
    int retain_network_instance(PointerByReference inst_names, long length);
    int collect_network_infos(KeyValuePair[] infos, long max_length);

}
