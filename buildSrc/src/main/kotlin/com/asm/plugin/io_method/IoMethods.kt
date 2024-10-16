package com.asm.plugin.io_method


val IoMethods = mutableListOf<IoMethodEntity>().apply {
    add(IoMethodEntity("java/lang/Object","wait","()V"))
    add(IoMethodEntity("java/lang/Object","wait","(J)V"))
    add(IoMethodEntity("java/lang/Object","wait","(JI)V"))

    add(IoMethodEntity("java/lang/Thread","start","()V"))
    add(IoMethodEntity("java/lang/Thread","sleep","(J)V"))
    add(IoMethodEntity("java/lang/Thread","sleep","(JI)V"))

    add(IoMethodEntity("java/lang/ClassLoader","getResource","(Ljava/lang/String;)Ljava/net/URL;"))
    add(IoMethodEntity("java/lang/ClassLoader","getResources","(Ljava/lang/String;)Ljava/util/Enumeration;"))
    add(IoMethodEntity("java/lang/ClassLoader","getResourceAsStream","(Ljava/lang/String;)Ljava/io/InputStream;"))
    add(IoMethodEntity("java/lang/ClassLoader","getSystemResource","(Ljava/lang/String;)Ljava/net/URL;"))
    add(IoMethodEntity("java/lang/ClassLoader","getSystemResources","(Ljava/lang/String;)Ljava/util/Enumeration;"))
    add(IoMethodEntity("java/lang/ClassLoader","getSystemResourceAsStream","(Ljava/lang/String;)Ljava/io/InputStream;"))

//    add(IoMethodEntity("","",""))
}


val CHECK_THREAD_PACKAGE = "com/asm/privacymethodhooker"