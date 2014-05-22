package com.espendwise.manta.dao;

import com.espendwise.manta.model.view.CountryView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

import javax.persistence.EntityManager;
import java.util.List;


public class CountryDAOImpl extends DAOImpl implements CountryDAO {

    public CountryDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<CountryView> findAll(String... sortBy) {

        String sortByExp = Constants.EMPTY;
        if (Utility.isSet(sortBy)) {
            for (String s : sortBy) {
                sortByExp += Utility.isSet(sortByExp) ? "," : "";
                sortByExp += "c." + s;
            }
        }

        javax.persistence.Query q = em.createQuery(
                "Select  new com.espendwise.manta.model.view.CountryView(" +
                        "c.id, c.shortDesc, c.uiName, c.countryCode, c.localeCd, " +
                        "c.inputDateFormat, c.inputTimeFormat, c.inputDayMonthFormat, " +
                        "p.value) " +
                        "from CountryFullEntity c " +
                        "left join c.countryProperties p with p.countryPropertyCd=" + QueryHelp.toQuoted(RefCodeNames.COUNTRY_PROPERTY.USES_STATE) + " " +
                        (Utility.isSet(sortByExp)?" order by "+sortByExp:"")
        );

        return (List<CountryView>)q.getResultList();
    }

}
