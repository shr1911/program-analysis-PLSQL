CREATE OR REPLACE PROCEDURE vulnerable(pname IN VARCHAR2, vresult OUT VARCHAR2) AS
	vsql VARCHAR2(4000);
	test NUMBER(100);
	vname VARCHAR2(1000);
BEGIN
	test := 20;
	IF (test > 50) THEN
		vname := 'John';
	ELSIF (test > 30) THEN
		vname := 'Christina';
	ELSE
		vname := pname;
	END IF;
    vsql := 'SELECT description FROM products WHERE name=''' || vname || '''';
    EXECUTE IMMEDIATE vsql INTO vresult;
END;