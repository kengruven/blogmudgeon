INSERT INTO posts (uuid, blog_id, title, content, created, updated, published)
       VALUES ('53af2a7d-024e-43d5-b91d-881ae845449c',
               (select id from users where login = 'ken' limit 1),
               'Seattle Seahawks - Wikipedia, the free encyclopedia',
               'The Seattle Seahawks are a professional American football franchise based in Seattle, Washington. They are members of the National Football League (NFL) and the current Super Bowl champions. They are members of the NFC West division of the National Football Conference (NFC). The Seahawks joined the NFL in 1976 as an expansion team along with the Tampa Bay Buccaneers. The Seahawks are owned by Paul Allen, co-founder of Microsoft, and are currently coached by Pete Carroll. Since 2002, the Seahawks have played their home games at CenturyLink Field, located south of downtown Seattle. The Seahawks previously played home games in the Kingdome (1976–1999) and Husky Stadium (1994, 2000–2001).',
               CURRENT_TIMESTAMP,
               CURRENT_TIMESTAMP,
               TRUE),
              ('68bd3755-a0ae-4951-acde-a74b2a25756a',
               (select id from users where login = 'ken' limit 1),
               'シアトル・シーホークス - Wikipedia',
               'シアトル・シーホークス (Seattle Seahawks) は、ワシントン州シアトルに本拠地を置くNFLチームである。2002年のリーグ再編に際しAFC西地区からNFC西地区に移動した。オーナーはポール・アレン。現時点でAFCチャンピオンシップゲーム、NFCチャンピオンシップゲームに出場した唯一のチーム。第40回スーパーボウルに出場している。',
               CURRENT_TIMESTAMP,
               CURRENT_TIMESTAMP,
               TRUE),
              ('fd468e32-e714-4254-a54a-ff638e01cd12',
               (select id from users where login = 'ken' limit 1),
               'National Football League - Wikipedia, the free encyclopedia',
               'The National Football League (NFL) is a professional American football league that constitutes one of the four major professional sports leagues in North America. It is composed of 32 teams divided equally between the National Football Conference (NFC) and the American Football Conference (AFC). The highest professional level of the sport in the world,[4] the NFL runs a 17-week regular season from the week after Labor Day to the week after Christmas, with each team playing sixteen games and having one bye week each season. Out of the league''s 32 teams, six (four division winners and two wild-card teams) from each conference compete in the NFL playoffs, a single-elimination tournament culminating in the Super Bowl, played between the champions of the NFC and AFC. The champions of the Super Bowl are awarded the Vince Lombardi Trophy. Various other awards exist to recognize individual players and coaches. Most games are played on Sunday afternoons; some games are also played on Mondays and Thursdays during the regular season. There are games on Saturdays during the first two playoff weekends. Sometimes, there are also Saturday games during the last few weeks of the regular season.',
               CURRENT_TIMESTAMP,
               CURRENT_TIMESTAMP,
               TRUE);
