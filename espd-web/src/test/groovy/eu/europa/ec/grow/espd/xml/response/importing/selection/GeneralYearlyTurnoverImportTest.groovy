/*
 *
 * Copyright 2016 EUROPEAN COMMISSION
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/community/eupl/og_page/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 *
 */

package eu.europa.ec.grow.espd.xml.response.importing.selection

import eu.europa.ec.grow.espd.domain.AvailableElectronically
import eu.europa.ec.grow.espd.domain.DynamicRequirementGroup
import eu.europa.ec.grow.espd.domain.EconomicFinancialStandingCriterion
import eu.europa.ec.grow.espd.domain.EspdDocument
import eu.europa.ec.grow.espd.xml.base.AbstractXmlFileImport
import eu.europa.ec.grow.espd.xml.LocalDateAdapter

/**
 * Created by ratoico on 1/8/16 at 2:44 PM.
 */
class GeneralYearlyTurnoverImportTest extends AbstractXmlFileImport {

    def "06. should import all fields of 'General yearly turnover'"() {
        when:
        EspdDocument espd = parseXmlResponseFile("selection/general_yearly_turnover_import.xml")
        def unboundedGroups = espd.generalYearlyTurnover.unboundedGroups
		
        then:
        unboundedGroups.size() == 5

        then:
        espd.generalYearlyTurnover.exists == true
        espd.generalYearlyTurnover.answer == true
				
		then:
		unboundedGroups[0].get("startDate") == LocalDateAdapter.unmarshal("2015-05-01").toDate()
		unboundedGroups[0].get("endDate") == LocalDateAdapter.unmarshal("2016-04-30").toDate()
		unboundedGroups[0].get("amount") == 111.1
		unboundedGroups[0].get("currency") == "RON"

		then:
		unboundedGroups[1].get("startDate") == LocalDateAdapter.unmarshal("2014-05-01").toDate()
		unboundedGroups[1].get("endDate") == LocalDateAdapter.unmarshal("2015-04-30").toDate()
		unboundedGroups[1].get("amount") == 222.2
		unboundedGroups[1].get("currency") == "EUR"
		
		then:
		unboundedGroups[2].get("startDate") == LocalDateAdapter.unmarshal("2013-05-01").toDate()
		unboundedGroups[2].get("endDate") == LocalDateAdapter.unmarshal("2014-04-30").toDate()
		unboundedGroups[2].get("amount") == 333.3
		unboundedGroups[2].get("currency") == "USD"

		then:
		unboundedGroups[3].get("startDate") == LocalDateAdapter.unmarshal("2012-05-01").toDate()
		unboundedGroups[3].get("endDate") == LocalDateAdapter.unmarshal("2013-04-30").toDate()
		unboundedGroups[3].get("amount") == 444.4
		unboundedGroups[3].get("currency") == "CHF"

		then:
		unboundedGroups[4].get("startDate") == LocalDateAdapter.unmarshal("2011-05-01").toDate()
		unboundedGroups[4].get("endDate") == LocalDateAdapter.unmarshal("2012-04-30").toDate()
		unboundedGroups[4].get("amount") == 555.5
		unboundedGroups[4].get("currency") == "YEN"

        then: "info electronically"
        espd.generalYearlyTurnover.availableElectronically.answer == true
        espd.generalYearlyTurnover.availableElectronically.url == "www.hodor.com"
        espd.generalYearlyTurnover.availableElectronically.code == "GENERAL_TURNOVER"
        espd.generalYearlyTurnover.availableElectronically.issuer == "HODOR"
    }

	
    def "a selection criterion with no answer will be treated as FALSE"() {
        when:
        EspdDocument espd = parseXmlResponseFile("selection/selection_criterion_no_answer_import.xml")

        then:
        espd.generalYearlyTurnover.exists == true
        espd.generalYearlyTurnover.answer == false

    }

    def "all fields needed to generate a XML sample"() {
        given:
        def espd = new EspdDocument(generalYearlyTurnover: new EconomicFinancialStandingCriterion(exists: true, answer: true,
                unboundedGroups: [
                        new DynamicRequirementGroup("year": 2016, "amount": 111.1, "currency": "RON"),
                        new DynamicRequirementGroup("year": 2015, "amount": 222.2, "currency": "EUR"),
                        new DynamicRequirementGroup("year": 2014, "amount": 333.3, "currency": "USD"),
                        new DynamicRequirementGroup("year": 2013, "amount": 444.4, "currency": "CHF"),
                        new DynamicRequirementGroup("year": 2012, "amount": 555.5, "currency": "YEN")],
                availableElectronically: new AvailableElectronically(answer: true, url: "www.hodor.com", code: "GENERAL_TURNOVER", issuer: "HODOR")))
//                saveEspdAsXmlResponse(espd, "/home/ratoico/Downloads/espd-response.xml")

        expect:
        1 == 1
    }

}