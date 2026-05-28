package com.jujutsu.archive.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportParametersTest {

    @Test
    void defaultValues_ShouldBeCorrect() {
        ReportParameters params = new ReportParameters();

        assertTrue(params.isShowBasic());
        assertTrue(params.isShowCurse());
        assertTrue(params.isShowSorcerers());
        assertTrue(params.isShowTechniques());
        assertFalse(params.isShowExtensions());
    }

    @Test
    void setters_ShouldUpdateValues() {
        ReportParameters params = new ReportParameters();
        params.setShowBasic(false);
        params.setShowCurse(false);
        params.setShowSorcerers(false);
        params.setShowTechniques(false);
        params.setShowExtensions(true);

        assertFalse(params.isShowBasic());
        assertFalse(params.isShowCurse());
        assertFalse(params.isShowSorcerers());
        assertFalse(params.isShowTechniques());
        assertTrue(params.isShowExtensions());
    }
}