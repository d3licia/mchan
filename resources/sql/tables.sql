-- Table: public.posts

-- DROP TABLE public.posts;

CREATE TABLE public.posts
(
  id integer NOT NULL DEFAULT nextval('posts_id_seq'::regclass),
  board character varying NOT NULL,
  name character varying,
  email character varying,
  subject character varying,
  content character varying,
  thread integer NOT NULL,
  op boolean NOT NULL DEFAULT false,
  "timestamp" timestamp without time zone,
  CONSTRAINT posts_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.posts
  OWNER TO "mlab";

-- Index: public.index_posts_on_id_and_board

-- DROP INDEX public.index_posts_on_id_and_board;

CREATE UNIQUE INDEX index_posts_on_id_and_board
  ON public.posts
  USING btree
  (id, board COLLATE pg_catalog."default");

