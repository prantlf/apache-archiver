COMMONS_JAR=commons-compress-1.18.jar
COMMONS_TARDIR=commons-compress-1.18
COMMONS_TAR=$(COMMONS_TARDIR)-bin.tar.gz
COMMONS_TARNEW=$(COMMONS_TARDIR)-test.tar
COMMONS_ZIPDIR=commons-compress-1.17
COMMONS_ZIP=$(COMMONS_ZIPDIR)-bin.zip
COMMONS_ZIPNEW=$(COMMONS_ZIPDIR)-test.zip

COMPILE=javac -classpath $(COMMONS_JAR)
EXECUTE=java -cp $(COMMONS_JAR):.

all: untar.class unzip.class tar.class zip.class

$(COMMONS_TAR):
	curl -o $(COMMONS_TAR) https://www-eu.apache.org/dist/commons/compress/binaries/$(COMMONS_TAR)

$(COMMONS_ZIP):
	curl -o $(COMMONS_ZIP) http://archive.apache.org/dist/commons/compress/binaries/$(COMMONS_ZIP)

$(COMMONS_JAR): $(COMMONS_TAR)
	tar xf $(COMMONS_TAR) $(COMMONS_TARDIR)/$(COMMONS_JAR) --strip-components=1

untar.class: untar.java $(COMMONS_JAR)
	$(COMPILE) $<

unzip.class: unzip.java $(COMMONS_JAR)
	$(COMPILE) $<

tar.class: tar.java $(COMMONS_JAR)
	$(COMPILE) $<

zip.class: zip.java $(COMMONS_JAR)
	$(COMPILE) $<

test: list uncompress compress

list: list_tar list_zip

list_tar: $(COMMONS_TAR) untar.class
	$(EXECUTE) untar l $<

list_zip: $(COMMONS_ZIP) unzip.class
	$(EXECUTE) unzip l $<

uncompress: uncompress_tar uncompress_zip

uncompress_tar: $(COMMONS_TAR) untar.class
	$(EXECUTE) untar x $<

uncompress_zip: $(COMMONS_ZIP) unzip.class
	$(EXECUTE) unzip x $<

compress: compress_tar compress_zip

compress_tar: $(COMMONS_TARDIR) tar.class
	$(EXECUTE) tar $(COMMONS_TARNEW) $<

compress_zip: $(COMMONS_ZIPDIR) zip.class
	$(EXECUTE) zip $(COMMONS_ZIPNEW) $<

clean:
	rm -rf *.class $(COMMONS_TARDIR) $(COMMONS_TARNEW) $(COMMONS_ZIPDIR) $(COMMONS_ZIPNEW)

distclean: clean
	rm -f $(COMMONS_JAR) $(COMMONS_TAR) $(COMMONS_ZIP)
