select url_id, count(*) as total_hits from hits
where url_id = 3 and YEAR(created_at) = 2013 and MONTH(created_at)=4
group by YEAR(created_at), MONTH(created_at)


SELECT
URLS.MURL, count(HITS.created_at)
FROM URLS
INNER JOIN HITS ON URLS.ID = HITS.URL_ID
and YEAR(HITS.created_at) = 2013
group by MONTH(HITS.created_at)
order by MONTH(HITS.created_at) ASC

SELECT
MONTH(HITS.created_at) as month, count(HITS.created_at) as hits
FROM URLS
INNER JOIN HITS ON URLS.ID = HITS.URL_ID
and YEAR(HITS.created_at) = 2013
where URLS.MURL = '4AXER24P'
group by MONTH(HITS.created_at)
order by MONTH(HITS.created_at) ASC
