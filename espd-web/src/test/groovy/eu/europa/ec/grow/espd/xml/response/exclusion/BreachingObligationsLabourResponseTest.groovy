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

package eu.europa.ec.grow.espd.xml.response.exclusion

import eu.europa.ec.grow.espd.domain.enums.criteria.ExclusionCriterion
import eu.europa.ec.grow.espd.domain.EspdDocument
import eu.europa.ec.grow.espd.domain.LawCriterion
import eu.europa.ec.grow.espd.domain.SelfCleaning
import eu.europa.ec.grow.espd.xml.base.AbstractExclusionCriteriaFixture
/**
 * Created by ratoico on 1/11/16 at 11:17 AM.
 */
class BreachingObligationsLabourResponseTest extends AbstractExclusionCriteriaFixture {

    def "11. should contain the 'Breaching of obligations in the fields of labour law' criterion"() {
        given:
        def espd = new EspdDocument(breachingObligationsLabour: new LawCriterion(exists: true))

        when:
        def response = generateResponseXml(espd)
        def idx = getResponseCriterionIndex(ExclusionCriterion.BREACHING_OF_OBLIGATIONS_LABOUR)

        then: "CriterionID element"
        checkCriterionId(response, idx, "a34b70d6-c43d-4726-9a88-8e2b438424bf")

        then: "CriterionTypeCode element"
        checkCriterionTypeCode(response, idx, "CRITERION.EXCLUSION.SOCIAL.LABOUR_LAW")

        then: "CriterionName element"
        response.Criterion[idx].Name.text() == "Breaching of obligations in the fields of labour law"

        then: "CriterionDescription element"
        response.Criterion[idx].Description.text() == "Has the economic operator, to its knowledge, breached its obligations in the fields of labour law? As referred to for the purposes of this procurement in national law, in the relevant notice or the procurement documents or in Article 18(2) of Directive 2014/24/EU."

        then: "CriterionLegislationReference element"
        checkLegislationReference(response, idx, "57(4)")

        then: "check all the sub groups"
        response.Criterion[idx].RequirementGroup.size() == 1

        then: "G1"
        def g1 = response.Criterion[idx].RequirementGroup[0]
        g1.ID.text() == "976b5acb-c00f-46ca-8f83-5ce6abfdfe43"
        g1.@pi.text() == ""
        g1.RequirementGroup.size() == 1
        g1.Requirement.size() == 1
        def r1_0 = g1.Requirement[0]
        checkRequirement(r1_0, "974c8196-9d1c-419c-9ca9-45bb9f5fd59a", "Your answer?", "INDICATOR")

        then:
        def g1_1 = g1.RequirementGroup[0]
        g1_1.ID.text() == "64a2102c-4af1-4ecb-97b3-0c41907ec0f6"
        g1_1.@pi.text() == "GROUP_FULFILLED.ON_TRUE"
        def r1_1 = g1_1.Requirement[0]
        checkRequirement(r1_1, "e098da8e-4717-4500-965f-f882d5b4e1ad", "Please describe them", "DESCRIPTION")

        then: "check first sub group"
        def sub1_1 = g1_1.RequirementGroup[0]
        checkSelfCleaningRequirementGroup(sub1_1)
    }

    def "check the 'Your answer' requirement response"() {
        given:
        def espd = new EspdDocument(breachingObligationsLabour: new LawCriterion(exists: true, answer: false))

        when:
        def response = generateResponseXml(espd)
        def idx = getResponseCriterionIndex(ExclusionCriterion.BREACHING_OF_OBLIGATIONS_LABOUR)

        then:
        def req = response.Criterion[idx].RequirementGroup[0].Requirement[0]
        checkRequirement(req, "974c8196-9d1c-419c-9ca9-45bb9f5fd59a", "Your answer?", "INDICATOR")
        req.Response.size() == 1
        req.Response[0].Indicator.text() == "false"
    }

    def "check the 'Please describe them' requirement response"() {
        given:
        def espd = new EspdDocument(breachingObligationsLabour: new LawCriterion(exists: true,
                description: "bogus description."))

        when:
        def response = generateResponseXml(espd)
        def idx = getResponseCriterionIndex(ExclusionCriterion.BREACHING_OF_OBLIGATIONS_LABOUR)

        then:
        def subGroup = response.Criterion[idx].RequirementGroup[0].RequirementGroup[0]

        def req = subGroup.Requirement[0]
        checkRequirement(req, "e098da8e-4717-4500-965f-f882d5b4e1ad", "Please describe them", "DESCRIPTION")
        req.Response[0].Description.text() == "bogus description."
    }

    def "check the 'Have you taken measures to demonstrate your reliability (\"Self-Cleaning\")' requirement response"() {
        given:
        def espd = new EspdDocument(breachingObligationsLabour: new LawCriterion(exists: true,
                selfCleaning: new SelfCleaning(answer: false)))

        when:
        def response = generateResponseXml(espd)
        def idx = getResponseCriterionIndex(ExclusionCriterion.BREACHING_OF_OBLIGATIONS_LABOUR)

        then:
        def subGroup = response.Criterion[idx].RequirementGroup[0].RequirementGroup[0].RequirementGroup[0]

        def req = subGroup.Requirement[0]
        req.Response.size() == 1
        req.Response[0].Indicator.text() == "false"
    }

    def "check the 'Self cleaning description' requirement response"() {
        given:
        def espd = new EspdDocument(breachingObligationsLabour: new LawCriterion(exists: true,
                selfCleaning: new SelfCleaning(description: "Hodor_11 is clean")))

        when:
        def response = generateResponseXml(espd)
        def idx = getResponseCriterionIndex(ExclusionCriterion.BREACHING_OF_OBLIGATIONS_LABOUR)

        then:
        def subGroup = response.Criterion[idx].RequirementGroup[0].RequirementGroup[0].RequirementGroup[0].RequirementGroup[0]

        def req = subGroup.Requirement[0]
        req.Response.size() == 1
        req.Response[0].Description.text() == "Hodor_11 is clean"
    }
    
}