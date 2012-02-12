What is this all about?
========================

It's a Java class file processor that can remove method calls, substiture one method call for another,
replace static access or add location information to existing method calls.

Why would I remove method calls?
================================

Let's say you want to write some software, but then strip out all or some of the debugging or assertions before
shipping the production version, either to reduce class size or reduce execution overhead.


What about adding location information?
======================================

When shipping production code, you might want to replace one implementation of a logger with another, say that
integrated with some other layer of your production platform. You can cheaply determine the execution point of the code,
(because it is happening as part of the build process) making it cheap to log.


What are the Java requirements?
-------------------------------

Not much - whatever your platform runs on. The transformed classes will run on the same JVM version as the original.
The file processor runs on your build system, so the runtime cost of the transformations is zero. Of course you could
plug this transformer in as part of the Java Instrumentation API - See http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/instrument/ClassFileTransformer.html


