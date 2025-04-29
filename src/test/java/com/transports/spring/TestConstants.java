package com.transports.spring;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class TestConstants {

    public static final String APP_URL = "http://localhost:8080";

    protected Playwright playwright;
    protected Browser browser;
    protected Page page;
}