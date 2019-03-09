COMMONS_JAR=commons-compress-1.18.jar
COMMONS_TARDIR=commons-compress-1.18
COMMONS_TAR=$(COMMONS_TARDIR)-bin.tar.gz
COMMONS_ZIPDIR=commons-compress-1.17
COMMONS_ZIP=$(COMMONS_ZIPDIR)-bin.zip

COMPILE=javac -classpath $(COMMONS_JAR)
EXECUTE=java -cp $(COMMONS_JAR):.

all: untar.class unzip.class

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

test: list uncompress

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

clean:
	rm -rf *.class $(COMMONS_TARDIR) $(COMMONS_ZIPDIR)

distclean: clean
	rm -f $(COMMONS_JAR) $(COMMONS_TAR) $(COMMONS_ZIP)
