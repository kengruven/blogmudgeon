blogmudgeon
===========

This is a blog I'm writing, because I'm not happy with any existing
blog, and also as an excuse to play with Clojure.

It's completely unusable as a blog, yet.  This is a from-scratch
project I'm doing in public, *not* a working software product I'm
open-sourcing.

Goals:
- As simple as possible, internally and externally, for readers, writers, and maintainers
- Runs on modern browsers (incl mobile) -- no particular backwards-compat goals
- Only needs Postgres and Java runtime on the server to run
- Only needs Postgres, Java, and Leiningen for development (plus whatever lein pulls in automatically)
- Readers can:
  - read articles
  - see stable, readable, typeable URLs
  - search for text
  - see what's new or changed recently (RSS/Atom, maybe a calendar)
- The writer can:
  - Create new drafts, publish (or un-publish) posts, and edit drafts/posts at any time
  - Work entirely in Markdown
  - Do very basic customization (blog name, name/email on contact page, maybe color)

Non-goals:
- Multiple users, or multiple blogs -- run your own copy!
- Extreme customization capabilities
- Photos and multimedia -- I may add the ability to attach images, eventually, but it's not going to be the primary goal, and it's not for you if you're going to want to attach big stylish stock photos everywhere
- Social media integration -- gag
- Comments -- if a reader wants to reply, they can use email; I've not seen a blog with comments that I thought were worthwhile, overall
- Banner advertisements -- again, not my thing
- Tags? -- maybe later, but not a primary concern


Installation
------------

This is how I installed it on a Debian host.  It's certainly not the
*best* way, but it worked for me.  Improvements to this process are
welcome.

On the server:
- apt-get update
- apt-get install nginx openjdk-7-jre daemontools{,-run}
- adduser blogmudgeon
- mkdir /home/blogmudgeon/releases

On the development machine:
- lein ring uberjar
- scp the jar to the server, as /home/blogmudgeon/releases/blogmudgeon-0.0.1-SNAPSHOT-standalone.jar

Back on the server:
- in ~blogmudgeon/releases, ln -s blogmudgeon-0.0.1-SNAPSHOT-standalone.jar blogmudgeon-latest.jar
- mkdir /etc/service/blogmudgeon (and chown it to the user, and everything below it)
- create /etc/service/blogmudgeon/run, with this script:

        #!/bin/sh
        JAVA_HOME=/usr
        JAVA_BIN=$JAVA_HOME/bin/java
        
        BLOGMUDGEON_USER=blogmudgeon
        BLOGMUDGEON_JAR=/home/blogmudgeon/releases/blogmudgeon-latest.jar
        exec 2>&1
        exec setuidgid $BLOGMUDGEON_USER $JAVA_BIN -jar $BLOGMUDGEON_JAR

To upload a new version, just re-uberjar it, scp it to the server (and
make sure the blogmudgeon-latest.jar symlink points to it), and kill
the old Java process.

Server nginx config:
- make a new file /etc/nginx/sites-available/blogmudgeon that looks like:

        server {
            listen 80;
            server_name your_blog_name.com *.your_blog_name.com;
            location / {
                proxy_pass http://localhost:3000;
            }
        }
- ln -s /etc/nginx/sites-available/blogmudgeon /etc/nginx/sites-enabled/blogmudgeon

Database, for development on OS X:
- install Homebrew, if you haven't already
- brew install postgresql
- start it <http://stackoverflow.com/a/7975660>:

        pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/server.log start

- (do you need to run initdb here first?)
- createdb YOUR_BLOG_NAME

Database, on the server:
- apt-get install postgresql
- (do you need to run initdb here first?)
- createdb YOUR_BLOG_NAME

TODO: migrations!
