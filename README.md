pyjvm
=====

Installation
--------

	git clone git://github.com/zielmicha/pyjvm
	cd pyjvm
	ln -s $PWD/pyjvm ~/bin/pyjvm

Usage
-----

    # create project
    pyjvm -p ~/pyjvm_example create
    # run project
    pyjvm -p ~/pyjvm_example run
    # build and run JAR
    pyjvm -p ~/pyjvm_example jar
    java -jar ~/pyjvm_example/build/build.jar
