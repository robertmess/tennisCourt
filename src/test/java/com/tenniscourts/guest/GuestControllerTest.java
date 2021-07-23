package com.tenniscourts.guest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.guests.GuestDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.tenniscourts.utils.TConstants.GUEST_URL;
import static org.junit.Assert.assertEquals;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GuestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void findAllGuests() throws Exception {
        mockMvc.perform(get(GUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Roger Federer"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Rafael Nadal"));
    }

    @Test
    public void findGuestById() throws Exception {
        String url = String.format("%s/{guestId}", GUEST_URL);

        mockMvc.perform(get(url, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Roger Federer"));
    }

    @Test
    public void findGuestByName() throws Exception {
        String url = String.format("%s/name/{name}", GUEST_URL);

        mockMvc.perform(get(url, "Rafael Nadal"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Rafael Nadal"));
    }

    @Test
    public void addGuest() throws Exception {
        GuestDTO guestDTO = setUpNewGuest("Thomaz Koch");

         MvcResult result = mockMvc.perform(post(GUEST_URL)
                .content(asJsonString(guestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        GuestDTO guestDTOResponse = objectMapper.readValue(content, GuestDTO.class);

        assertEquals(guestDTOResponse.getName(), guestDTO.getName());
    }

    @Test
    public void updateGuest() throws Exception {
        GuestDTO guestDTO = setUpNewGuestForUpdate(3L, "Gustavo Kuerten");

        MvcResult result = mockMvc.perform(put(GUEST_URL)
                .content(asJsonString(guestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        GuestDTO guestDTOResponse = objectMapper.readValue(content, GuestDTO.class);

        assertEquals(guestDTOResponse.getName(), guestDTO.getName());
    }

    @Test
    public void deleteGuest() throws Exception {
        MvcResult result = mockMvc.perform(post(GUEST_URL)
                .content(asJsonString(setUpNewGuest("Ilie Nastase")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        GuestDTO guestDTOResponse = objectMapper.readValue(content, GuestDTO.class);

        String url = String.format("%s/{guestId}", GUEST_URL);

        mockMvc.perform(delete(url,guestDTOResponse.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    protected String asJsonString(final Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    protected GuestDTO setUpNewGuest(final String name) {
        return GuestDTO.builder()
                .name(name)
                .build();
    }

    protected GuestDTO setUpNewGuestForUpdate(final Long id, final String name) {
        return GuestDTO.builder()
                .id(id)
                .name(name)
                .build();
    }

}
