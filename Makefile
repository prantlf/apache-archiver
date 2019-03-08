COMMONS_DIR=commons-compress-1.18
COMMONS_JAR=commons-compress-1.18.jar
COMMONS_TAR=commons-compress-1.18-bin.tar.gz

all: untar.class

$(COMMONS_TAR):
	curl -o $(COMMONS_TAR) https://www-eu.apache.org/dist//commons/compress/binaries/$(COMMONS_TAR)

$(COMMONS_JAR): $(COMMONS_TAR)
	tar xf $(COMMONS_TAR) $(COMMONS_DIR)/$(COMMONS_JAR) --strip-components=1

untar.class: $(COMMONS_JAR) untar.java
	javac -classpath $(COMMONS_JAR) untar.java

test: untar.class
	java -cp $(COMMONS_JAR):. untar l $(COMMONS_TAR) .
	java -cp $(COMMONS_JAR):. untar x $(COMMONS_TAR) .

clean:
	rm -rf $(COMMONS_DIR)

distclean: clean
	rm -f $(COMMONS_JAR) $(COMMONS_TAR)
