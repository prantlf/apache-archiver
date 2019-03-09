apache-archiver
===============

Simple compressing and decompressing command-line tools using [Apache Commons Compress]. You can use them as source code examples too.

How to Use
----------

Run the command-line tools without parameters to see the usage description:

```text
$ java -cp commons-compress-1.18.jar:. untar
Usage: untar l|x <input archive> [output directory]

$ java -cp commons-compress-1.18.jar:. unzip
Usage: unzip l|x <input archive> [output directory]

$ java -cp commons-compress-1.18.jar:. tar
Usage: tar <output archive> [input directory]

$ java -cp commons-compress-1.18.jar:. zip
Usage: zip <output archive> [input directory]
```

List contents of a TAR.GZ archive:

```text
$ java -cp commons-compress-1.18.jar:. untar l commons-compress-1.18-bin.tar.gz
File "commons-compress-1.18/LICENSE.txt"
...
```

Uncompress a ZIP archive to the current directory:

```text
$ java -cp commons-compress-1.18.jar:. unzip x commons-compress-1.18-bin.zip .
Unpacking "commons-compress-1.18/LICENSE.txt"...
...
```

Compress a TAR archive of the current directory:

```text
$ java -cp commons-compress-1.18.jar:. tar test.tar
Packing "./LICENSE"...
...
```

How to Build
------------

Make sure, that you installed [OpenJDK Java] and [GNU Make] or compatibles. The following command will download the `common-compress` package and build the command line tools:

```sh
make all
```

The following command will test, that the command-line tools work:

```sh
make test
```

The following command will remove build and test output files:

```sh
make clean
```

The following command will remove all files created by the `Makefile`, including the downloaded `commons-compress` package:

```sh
make distclean
```

Contributing
------------

In lieu of a formal styleguide, take care to maintain the existing coding style.

License
-------

Copyright (c) 2019 Ferdinand Prantl

Licensed under the Apache 2.0 license.

[Apache Commons Compress]: https://commons.apache.org/proper/commons-compress/
[OpenJDK Java]: https://openjdk.java.net/
[GNU Make]: https://www.gnu.org/software/make/
