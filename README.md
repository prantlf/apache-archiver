apache-archiver
===============

Simple compressing and decompressing command-line tools using [Apache Commons Compress].

How to Use
----------

Run the command-line tools without parameters to see the usage description:

```text
$ java -cp commons-compress-1.18.jar:. untar
Usage: untar [l|x] <input archive> <output directory>
```

List contents of a TAR.GZ archive:

```text
$ java -cp commons-compress-1.18.jar:. untar l commons-compress-1.18-bin.tar.gz
File "commons-compress-1.18/LICENSE.txt"
...
```

Uncompress a TAR.GZ archive to the current directory:

```text
$ java -cp commons-compress-1.18.jar:. untar x commons-compress-1.18-bin.tar.gz .
Unpacking "commons-compress-1.18/LICENSE.txt"...
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

The following command will remove all files created by the build script, including the downloaded `commons-compress` package:

```sh
make distclean
```

[Apache Commons Compress]: https://commons.apache.org/proper/commons-compress/
[Java]: https://openjdk.java.net/
[GNU Make]: https://www.gnu.org/software/make/
