package com.tripj.domain.trip.service;

import com.tripj.domain.country.model.entity.Country;
import com.tripj.domain.country.repository.CountryRepository;
import com.tripj.domain.trip.model.dto.CreateTripRequest;
import com.tripj.domain.trip.model.dto.CreateTripResponse;
import com.tripj.domain.trip.model.dto.GetTripResponse;
import com.tripj.domain.trip.model.entity.Trip;
import com.tripj.domain.trip.repository.TripRepository;
import com.tripj.domain.user.model.entity.User;
import com.tripj.domain.user.repository.UserRepository;
import com.tripj.global.code.ErrorCode;
import com.tripj.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;

    public CreateTripResponse createTrip(CreateTripRequest request,
                                         Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다.", ErrorCode.E404_NOT_EXISTS_MEMBER));

        Country country = countryRepository.findById(request.getCountryId())
                        .orElseThrow(() -> new NotFoundException("존재하지 않는 나라입니다.", ErrorCode.E404_NOT_EXISTS_COUNTRY));

        Trip savedTrip = tripRepository.save(request.toEntity(user, country));

        return CreateTripResponse.of(savedTrip.getId());
    }


    /**
     * 여행 조회
     */
    @Transactional(readOnly = true)
    public List<GetTripResponse> getTrip(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다.", ErrorCode.E404_NOT_EXISTS_MEMBER));

        return tripRepository.getTrip(userId);
    }




}














