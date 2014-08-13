blogmudgeon
===========

This is a blog I'm writing, because I'm not happy with any existing
blog, and also as an excuse to play with Clojure.

Goals:
- As simple as possible, internally and externally, for readers, writers, and maintainers
- Runs on modern browsers (incl mobile) -- no particular backwards-compat goals
- Assume it's Postgres on the backend (i.e., not RDBMS-agnostic)
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
- Photos and multimedia
- Social media integration
