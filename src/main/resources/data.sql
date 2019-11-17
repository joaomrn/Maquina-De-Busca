insert into documento (id, frequenciaMaxima, somaQuadradosPesos, texto, url, visao) values
(1, 4, 25.6890245, "to do is to be to be is to do", "www.1.com.br", "to do is to be to be is to do"),
(6, 2, 24, "to be or not to be i am what i am", "www.2.com.br", "to be or not to be i am what i am"),
(12, 3, 14.1510208, "i think therefore i am do be do be do", "www.3.com.br", "i think therefore i am do be do be do"),
(15, 3, 59.87914532, "do do do da da da let it be let it be", "www.4.com.br", "do do do da da da let it be let it be");


insert into termodocumento (id, n, texto) values 
(2, 2, "to"),
(3, 3, "do"),
(4, 1, "is"),
(5, 4, "be"),
(7, 1, "or"),
(8, 1, "not"),
(9, 2, "i"),
(10, 2, "am"),
(11, 1, "what"),
(13, 1, "think"),
(14, 1, "therefore"),
(16, 1, "da"),
(17, 1, "let"),
(18, 1, "it");


insert into indiceinvertido (frequencia, peso, documento_id, termo_id) values
(4, 3, 1, 2),                       -- (1, to)
(2, 0.830074999, 1, 3),    -- (1, do)
(2, 4, 1, 4),                       -- (1, is)
(2, 0, 1, 5),                       -- (1, be)
(2, 2, 6, 2),                       -- (2, to)
(2, 0, 6, 5),                       -- (2, be)
(1, 2, 6, 7),                       -- (2, or)
(1, 2, 6, 8),                       -- (2, not)
(2, 2, 6, 9),                       -- (2, i) 
(2, 2, 6, 10),                     -- (2, am)
(1, 2, 6, 11),                     -- (2, what)
(3, 1.072856372, 12, 3),   -- (3, do)
(2, 0, 12, 5),                      -- (3, be)
(2, 2, 12, 9),                      -- (3, i)
(1, 1, 12, 10),                    -- (3, am)
(1, 2, 12, 13),                    -- (3, think)
(1, 2, 12, 14),                    -- (3, therefore)
(3, 1.072856372, 15, 3),   -- (4, do)
(2, 0, 15, 5),                      -- (4, be)
(3, 5.169925001, 15, 16), -- (4, da)
(2, 4, 15, 17),                    -- (4, let)
(2, 4, 15, 18);                    -- (4, it)
