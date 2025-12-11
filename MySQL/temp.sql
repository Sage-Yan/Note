use fbip81_devdb_ronghe_250430;

select pk_equip
from pam_equip pam_equip
         left outer join pam_category pam_category on pam_equip.pk_category = pam_category.pk_category
         left outer join pam_location pam_location on pam_equip.pk_location = pam_location.pk_location
         left outer join pam_priority pam_priority on pam_equip.pk_priority = pam_priority.pk_priority
         left outer join org_dept mandept on pam_equip.pk_mandept = mandept.pk_dept
         left outer join bd_psndoc mandoc on pam_equip.pk_manager = mandoc.pk_psndoc
         left outer join org_orgs assetorg on pam_equip.pk_usedunit = assetorg.pk_org
         left outer join org_dept dept on pam_equip.pk_usedept = dept.pk_dept
         left outer join bd_psndoc psndoc on pam_equip.pk_user = psndoc.pk_psndoc
         left outer join pam_specialty pam_specialty on pam_equip.pk_specialty = pam_specialty.pk_specialty
         left outer join pam_status pk_used_status on pam_equip.pk_used_status = pk_used_status.pk_status
where 1 = 1
  and (pam_equip.pk_category = '1001E110000000002FYJ' and 1 = 1 and pam_equip.pk_group = '0001E110000000000D73' and
       pam_equip.dr = 0)
  and pam_equip.pk_usedorg = '0001E110000000004YE0'
  and pam_equip.pk_used_status in ('')
order by pam_equip.equip_code


INSERT INTO org_orgs (ts, `enablestate`, `memo`, `ncindustry`, `tel`, `workcalendar`, `shortname2`, `shortname4`,
                      `shortname`, `shortname3`, `shortname6`, `shortname5`, `name6`, `name5`, `name4`, `name3`,
                      `entitytype`, `name2`, `orgtype49`, `orgtype48`, `orgtype47`, `orgtype46`, `pk_currtype`,
                      `isbalanceunit`, `countryzone`, `orgtype5`, `orgtype4`, `orgtype3`, `orgtype2`, `orgtype1`,
                      `def10`, `def11`, `def12`, `def13`, `def14`, `def15`, `orgtype50`, `def16`, `def17`, `orgtype9`,
                      `def18`, `orgtype8`, `def19`, `orgtype7`, `orgtype6`, `orgtype38`, `orgtype37`, `creator`,
                      `orgtype36`, `address`, `orgtype35`, `pk_format`, `orgtype39`, `pk_fatherorg`, `orgtype41`,
                      `orgtype40`, `orgtype45`, `orgtype44`, `orgtype43`, `orgtype42`, `pk_group`, `orgtype27`,
                      `orgtype26`, `orgtype25`, `orgtype24`, `reportconfirm`, `orgtype29`, `modifier`, `orgtype28`,
                      `islastversion`, `dr`, `pk_org`, `dataoriginflag`, `orgtype30`, `orgtype34`, `orgtype33`,
                      `orgtype32`, `orgtype31`, `orgtype16`, `orgtype15`, `orgtype14`, `organizationcode`, `orgtype13`,
                      `orgtype19`, `orgtype18`, `isbusinessunit`, `orgtype17`, `pk_controlarea`, `mnecode`,
                      `vstartdate`, `vname`, `def20`, `vno`, `name`, `isretail`, `creationtime`, `orgtype23`,
                      `orgtype22`, `orgtype21`, `orgtype20`, `pk_exratescheme`, `vname6`, `code`, `vname5`, `vname4`,
                      `vname3`, `vname2`, `pk_ownorg`, `innercode`, `principal`, `pk_accperiodscheme`, `modifiedtime`,
                      `venddate`, `pk_corp`, `pk_timezone`, `orgtype12`, `orgtype11`, `orgtype10`, `def3`, `def4`,
                      `pk_vid`, `def1`, `chargeleader`, `def2`, `def9`, `def7`, `def8`, `def5`, `def6`)
VALUES (null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null)